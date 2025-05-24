package vn.base.edumate.common.sheduled;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.base.edumate.image.Image;
import vn.base.edumate.image.ImageRepository;

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
