package vn.base.edumate.image;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.base.edumate.common.base.DataResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @PostMapping
    DataResponse<List<ImageResponse>> saveImage(@RequestParam List<MultipartFile> multipartFiles,
                                              @RequestParam Long postId) throws SQLException, IOException {
        List<ImageResponse> imageResponses =  imageService.saveImage(multipartFiles,postId);
        return DataResponse.<List<ImageResponse>>builder()
                .message("Thêm ảnh thành công!")
                .data(imageResponses).build();

    }
    @GetMapping("/{imageId}")
    ResponseEntity<Resource> downloadImage(@PathVariable("imageId") Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource byteArrayResource = imageService.getImageResource(image);
        return ResponseEntity.status(OK)
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" +image.getFileName() + "\"")
                .body(byteArrayResource);
    }
    @DeleteMapping("/{imageId}")
    DataResponse<Void> deleteImage(@PathVariable("imageId") Long imageId)  {
        return DataResponse.<Void>builder()
                .message("xóa ảnh thành công")
                .build();
    }
    @GetMapping("by-post/{id}")
    public DataResponse<List<ImageResponse>> findByPost(@PathVariable("id") Long postId)  {
        return DataResponse.<List<ImageResponse>>builder()
                .message("Tìm thành công!")
                .data(imageService.getImagesByPostId(postId))
                .build();
    }
}
