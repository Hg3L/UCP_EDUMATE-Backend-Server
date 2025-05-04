package vn.base.edumate.image;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ImageService {
    Image getImageById(Long id);

    void deleteById(Long id);

    List<ImageResponse> saveImage(List<MultipartFile> multipartFiles, Long postId) throws SQLException, IOException;

    List<ImageResponse> getImagesByPostId(Long postId);

    ByteArrayResource getImageResource(Image image) throws SQLException;


}
