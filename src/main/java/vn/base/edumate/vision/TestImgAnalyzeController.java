//package vn.base.edumate.vision;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import vn.base.edumate.common.base.DataResponse;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/test/vision-analyze")
//@RequiredArgsConstructor
//@Slf4j
//public class TestImgAnalyzeController {
//
//    private final ImageAnalyzeService imageAnalyzeService;
//
//    @PostMapping
//    public DataResponse<Map<String, String>> testImgAnalyze(@RequestParam("file") MultipartFile file) {
//        try {
//            Map<String, String> result = imageAnalyzeService.analyzeImage(file);
//            return DataResponse.<Map<String, String>>builder()
//                    .message("Image analysis successful")
//                    .data(result)
//                    .build();
//        } catch (Exception e) {
//            log.error("Error analyzing image", e);
//            return DataResponse.<Map<String, String>>builder()
//                    .message("Image analysis failed: " + e.getMessage())
//                    .build();
//        }
//    }
//}