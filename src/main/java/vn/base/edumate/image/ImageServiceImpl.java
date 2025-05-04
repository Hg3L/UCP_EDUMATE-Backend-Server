package vn.base.edumate.image;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.base.edumate.common.exception.BaseApplicationException;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.exception.ResourceNotFoundException;
import vn.base.edumate.post.*;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ImageServiceImpl implements ImageService {
    ImageRepository imageRepository;
    PostService postService;
    ImageMapper imageMapper;
    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorCode.IMAGE_NOT_EXISTED));
    }

    @Override
    public void deleteById(Long id) {
        Image image = getImageById(id);
        imageRepository.deleteById(image.getId());
    }

    @Override

    public List<ImageResponse> saveImage(List<MultipartFile> multipartFiles, Long postId) throws SQLException, IOException {
        List<ImageResponse> imageResponses = new ArrayList<>();
        Post post =  postService.getPostById(postId);
        try{
            for(MultipartFile file : multipartFiles){
                Image image = Image.builder()
                        .fileName(file.getOriginalFilename())
                        .imageBytes(new SerialBlob(file.getBytes()))
                        .fileType(file.getContentType())
                        .post(post)
                        .build();
                imageRepository.save(image);
                String imageUrl = "/image/"+image.getId();
                image.setImageUrl(imageUrl);
                imageRepository.save(image);
                ImageResponse imageResponse = imageMapper.toImageResponse(image);
                imageResponses.add(imageResponse);
            }
        }
        catch (IOException e) {
            throw new IOException("Lỗi I/O: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new SQLException("Lỗi SQL: " + e.getMessage(), e);
        }

        return imageResponses;
    }

    @Override
    public List<ImageResponse> getImagesByPostId(Long postId) {
        AtomicReference<List<ImageResponse>> imagesResponse = new AtomicReference<>(new ArrayList<>());
        imageRepository.findByPostId(postId)
                .filter(list -> !list.isEmpty())
                .ifPresentOrElse(images -> {
                    List<ImageResponse> imageResponses = images.stream()
                            .map(imageMapper::toImageResponse)
                            .toList();
                   imagesResponse.set(imageResponses);
                        },() -> {throw new BaseApplicationException(ErrorCode.IMAGE_NOT_EXISTED);}
                );
        return imagesResponse.get();

    }

    @Override
    @Transactional
    public ByteArrayResource getImageResource(Image image) throws SQLException {
        Blob blob = image.getImageBytes();
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        return new ByteArrayResource(bytes);
    }
}
