package vn.base.edumate.googleai.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

    private final GenerativeModel generativeModel;

    public String getAnswer(String prompt, List<MultipartFile> images) {
        try {
            List<Part> parts = new ArrayList<>();

            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    parts.add(PartMaker.fromMimeTypeAndData(
                            Objects.requireNonNull(file.getContentType()), file.getBytes()));
                }
            }

            Content content = Content.newBuilder()
                    .setRole("user")
                    .addParts(Part.newBuilder().setText(prompt))
                    .addAllParts(parts)
                    .build();

            GenerateContentResponse response = generativeModel.generateContent(List.of(content));

            return ResponseHandler.getText(response);
        } catch (Exception e) {
            log.error("Error while generating content: ", e);
            return e.getMessage();
        }
    }
}
