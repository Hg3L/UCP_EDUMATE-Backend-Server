package vn.base.edumate;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.google.cloud.vision.v1.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class EdumateBackendServerApplication {

    public static void main(String[] args) throws IOException, URISyntaxException {
        //        // Lấy file JSON từ resources (không dùng đường dẫn tuyệt đối)
        //        InputStream serviceAccountStream = EdumateBackendServerApplication.class
        //                .getClassLoader()
        //                .getResourceAsStream("google-vision/google-vision-service.json");
        //
        //        if (serviceAccountStream == null) {
        //            throw new RuntimeException("Không tìm thấy file google-vision-service.json trong resources");
        //        }
        //
        //        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
        //                .setCredentialsProvider(() -> ServiceAccountCredentials.fromStream(serviceAccountStream))
        //                .build();
        //
        //        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(settings)) {
        //            ByteString imgBytes = ByteString.readFrom(
        //                    EdumateBackendServerApplication.class.getClassLoader().getResourceAsStream("OSK.jpg"));
        //
        //            Image img = Image.newBuilder().setContent(imgBytes).build();
        //            Feature feat = Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build();
        //
        //            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
        //                    .addFeatures(feat)
        //                    .setImage(img)
        //                    .build();
        //
        //            List<AnnotateImageResponse> responses =
        // vision.batchAnnotateImages(List.of(request)).getResponsesList();
        //
        //            for (AnnotateImageResponse res : responses) {
        //                SafeSearchAnnotation annotation = res.getSafeSearchAnnotation();
        //                log.info ("Adult: " + annotation.getAdult());
        //                log.info("Violence: " + annotation.getViolence());
        //                log.info("Racy: " + annotation.getRacy());
        //            }
        //        }

        SpringApplication.run(EdumateBackendServerApplication.class, args);
    }
}
