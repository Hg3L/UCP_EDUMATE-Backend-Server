package vn.base.edumate.vision;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageAnalyzeServiceImpl implements ImageAnalyzeService {

    private final ImageAnnotatorClient imageAnnotatorClient;

    @Override
    public String analyzeImage(MultipartFile file) {

        try {
            ByteString imgBytes = ByteString.readFrom(file.getInputStream());
            Image img = Image.newBuilder().setContent(imgBytes).build();

            Feature textFeature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
            Feature labelFeature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(textFeature)
                    .addFeatures(labelFeature)
                    .setImage(img)
                    .build();

            AnnotateImageResponse response = imageAnnotatorClient.batchAnnotateImages(List.of(request))
                    .getResponses(0);

            if (response.hasError()) {
                log.error("Vision API error: {}", response.getError().getMessage());
                throw new RuntimeException("Vision API error: " + response.getError().getMessage());
            }

            if (!getText(response).isEmpty()) {
                return getText(response);
            }

            if (getText(response).isEmpty()
                    && !getLabels(response).isEmpty()) {
                return getLabels(response);
            }


        } catch (Exception e) {
            log.error("Error analyzing image: {}", e.getMessage());
        }

        return "";
    }

    private String getLabels(AnnotateImageResponse response) {
        String labels = response.getLabelAnnotationsList().stream()
                .map(EntityAnnotation::getDescription)
                .collect(Collectors.joining(", "));

        return labels.isEmpty() ? "" : labels;
    }

    private String getText(AnnotateImageResponse response) {
        String text = response.getTextAnnotationsList().stream()
                .map(EntityAnnotation::getDescription)
                .collect(Collectors.joining(" "));

        return text.isEmpty() ? "" : text;
    }
}
