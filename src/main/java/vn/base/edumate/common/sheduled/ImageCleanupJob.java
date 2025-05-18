package vn.base.edumate.common.sheduled;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.base.edumate.image.Image;
import vn.base.edumate.image.ImageRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageCleanupJob {

    ImageRepository imageRepository;

    @Scheduled(fixedRate = 360000) // mỗi 1 giờ
    public void deleteUnlinkedImages() {
        
        Instant cutoff = Instant.now().minus(24, ChronoUnit.HOURS); // xoa anh khong duoc dung sau 24h
        List<Image> oldImages = imageRepository.findImagesWithoutPostBefore(cutoff);

        if (!oldImages.isEmpty()) {
            imageRepository.deleteAll(oldImages);
            System.out.println("Đã xoá " + oldImages.size() + " ảnh chưa được dùng sau 24h");
        }
    }
}
