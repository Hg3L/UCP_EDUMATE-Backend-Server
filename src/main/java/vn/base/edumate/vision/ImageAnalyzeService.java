package vn.base.edumate.vision;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ImageAnalyzeService {
    Map<String, String> analyzeImage(MultipartFile file);
}
