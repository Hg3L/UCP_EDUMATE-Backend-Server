package vn.base.edumate.image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import javax.sql.rowset.serial.SerialBlob;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.base.edumate.common.exception.BaseApplicationException;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.exception.ResourceNotFoundException;
import vn.base.edumate.post.*;
import vn.base.edumate.vision.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ImageServiceImpl implements ImageService {
    final ImageRepository imageRepository;
    final ImageMapper imageMapper;
    final Cloudinary cloudinary;
    final ImageAnalyzeService imageAnalyzeService;

    final RestClient restClient;

    @Value("${system.default.client.fastapi-batch}")
    private String batchUrl;

    @Override
    public Image getImageById(Long id) {
        return imageRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.IMAGE_NOT_EXISTED));
    }

    @Override
    public void deleteById(Long id) {
        Image image = getImageById(id);
        imageRepository.deleteById(image.getId());
    }

    @Transactional
    @Override
    public List<ImageResponse> saveImage(List<MultipartFile> multipartFiles) throws SQLException, IOException {
        List<ImageResponse> imageResponses = new ArrayList<>();
        List<ImageAnalyze> imageAnalyzes = new ArrayList<>();
        try {
            for (MultipartFile file : multipartFiles) {
                Image image = Image.builder()
                        .fileName(file.getOriginalFilename())
                        .imageBytes(new SerialBlob(file.getBytes()))
                        .fileType(file.getContentType())
                        .build();
                imageRepository.save(image);
                log.info("- Image saved: {}", image.getId());
                String imageUrl = "/image/" + image.getId();
                image.setImageUrl(imageUrl);
                imageRepository.save(image);

                String analyzeResult = imageAnalyzeService.analyzeImage(file);
                imageAnalyzes.add(ImageAnalyze.builder()
                        .id(image.getId())
                        .content(analyzeResult)
                        .imageUrl(imageUrl)
                        .build());

                log.info("Mapped Image: fileName={}, fileType={}, imageUrl={}", image.getFileName(), image.getFileType(), image.getImageUrl());
                ImageResponse imageResponse = imageMapper.toImageResponse(image);
                imageResponses.add(imageResponse);
                log.info("- Image response: {}", imageResponse);
            }

            if (!imageAnalyzes.isEmpty()) {
                restClient.post()
                        .uri(batchUrl)
                        .body(ImageAnalyzeBatch.builder().images(imageAnalyzes).build())
                        .retrieve()
                        .body(Map.class);
            }

        } catch (IOException e) {
            throw new IOException("Lỗi I/O: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new SQLException("Lỗi SQL: " + e.getMessage(), e);
        }

        return imageResponses;
    }

    @Override
    public List<ImageResponse> getImagesByPostId(Long postId) {
        AtomicReference<List<ImageResponse>> imagesResponse = new AtomicReference<>(new ArrayList<>());
        imageRepository
                .findByPostId(postId)
                .filter(list -> !list.isEmpty())
                .ifPresentOrElse(
                        images -> {
                            List<ImageResponse> imageResponses = images.stream()
                                    .map(imageMapper::toImageResponse)
                                    .toList();
                            imagesResponse.set(imageResponses);
                        },
                        () -> {
                            throw new BaseApplicationException(ErrorCode.IMAGE_NOT_EXISTED);
                        });
        return imagesResponse.get();
    }

    @Override
    @Transactional
    public ByteArrayResource getImageResource(Image image) throws SQLException {
        Blob blob = image.getImageBytes();
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        return new ByteArrayResource(bytes);
    }

    @Override
    public String uploadImageCloudinary(MultipartFile file) throws IOException {
        String publicValue = generatePublicValue(file.getOriginalFilename());
        log.info("originalFileName is: {}", file.getOriginalFilename());
        log.info("publicValue is: {}", publicValue);
        String extension = getFileName(Objects.requireNonNull(file.getOriginalFilename()))[1];
        log.info("extension is: {}", extension);
        File fileUpload = convert(file);
        log.info("fileUpload is: {}", fileUpload);
        cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id", publicValue));
        cleanDisk(fileUpload);
        return cloudinary.url()
                .secure(true)
                .generate(StringUtils.join(publicValue, ".", extension));
    }

    private File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        File convFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()), getFileName(file.getOriginalFilename())[1]));
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }
        return convFile;
    }

    private void cleanDisk(File file) {
        try {
            log.info("file.toPath(): {}", file.toPath());
            Path filePath = file.toPath();
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Error");
        }
    }

    public String generatePublicValue(String originalName) {
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(), "_", fileName);
    }

    public String[] getFileName(String originalName) {
        return originalName.split("\\.");
    }
}
