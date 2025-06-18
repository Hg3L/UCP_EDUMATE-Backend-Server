package vn.base.edumate.history.ai;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.base.edumate.common.base.DataResponse;
import vn.base.edumate.post.PostResponse;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/history/ai-search")
@RequiredArgsConstructor
public class AISearchController {

    private final AISearchService aiSearchService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DataResponse<Void> addNewHistoryAISearch(@ModelAttribute AISearchRequest request) {
        aiSearchService.addNewHistoryAISearch(request);
        return DataResponse.<Void>builder()
                .message("Success")
                .build();
    }


    @GetMapping("/all/history")
    public ResponseEntity<List<AISearchResponse>> getHistories() {
        List<AISearchResponse> histories = aiSearchService.getHistoriesByUser();
        return ResponseEntity.ok(histories);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS))
                .body(aiSearchService.getImageById(id));
    }

    @DeleteMapping("/{id}")
    public DataResponse<Void> deleteHistory(@PathVariable Long id) {
        aiSearchService.deleteHistoryById(id);
        return DataResponse.<Void>builder()
                .message("Success")
                .build();
    }

    @DeleteMapping
    public DataResponse<Void> deleteAllHistories() {
        aiSearchService.deleteAllHistoriesByUser();
        return DataResponse.<Void>builder()
                .message("Success")
                .build();
    }
}
