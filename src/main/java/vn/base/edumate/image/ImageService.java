package vn.base.edumate.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image getImageById(Long id);

    void deleteById(Long id);

    List<ImageResponse> saveImage(List<MultipartFile> multipartFiles) throws SQLException, IOException;

    List<ImageResponse> getImagesByPostId(Long postId);

    ByteArrayResource getImageResource(Image image) throws SQLException;
}
