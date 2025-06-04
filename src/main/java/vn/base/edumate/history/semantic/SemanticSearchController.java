package vn.base.edumate.history.semantic;

import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/history/semantic-search")
@RequiredArgsConstructor
public class SemanticSearchController {

    private final SemanticSearchService semanticSearchService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addNewSemanticSearchHistory(SemanticSearchRequest request) {
        semanticSearchService.addNewSemanticSearchHistory(request);
    }

    @GetMapping
    public ResponseEntity<List<SemanticSearchResponse>> getHistories() {
        List<SemanticSearchResponse> histories = semanticSearchService.getHistoriesByUser();
        return ResponseEntity.ok(histories);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS))
                .body(semanticSearchService.getImageById(id));
    }

}
