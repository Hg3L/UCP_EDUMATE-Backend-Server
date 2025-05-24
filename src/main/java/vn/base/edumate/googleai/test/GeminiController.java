package vn.base.edumate.googleai.test;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.base.edumate.common.base.DataResponse;

@RestController
@RequestMapping("/v1/gemini")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping
    public DataResponse<String> getAnswer(
            @RequestParam String prompt, @RequestParam("images") List<MultipartFile> images) {
        String answer = geminiService.getAnswer(prompt, images);
        return DataResponse.<String>builder().message("Success").data(answer).build();
    }
}
