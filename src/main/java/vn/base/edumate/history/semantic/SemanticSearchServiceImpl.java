package vn.base.edumate.history.semantic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.exception.ResourceNotFoundException;
import vn.base.edumate.image.Image;
import vn.base.edumate.image.ImageRepository;
import vn.base.edumate.user.service.UserService;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SemanticSearchServiceImpl implements SemanticSearchService {

    private static final String DEFAULT_ENDPOINT_URL = "/history/semantic-search/image/";
    private final SemanticSearchRepository semanticSearchRepository;
    private final UserService userService;
    private final SemanticSearchMapper semanticSearchMapper;
    private final ImageRepository imageRepository;

    @Override
    public void addNewSemanticSearchHistory(SemanticSearchRequest request) {
        log.info("Adding new semantic search history: {}", request);

        try {
            SemanticSearch semanticSearch = SemanticSearch.builder()
                    .user(userService.getUserById(request.getUid()))
                    .imageBytes(new SerialBlob(request.getImage().getBytes()))
                    .build();

            semanticSearchRepository.save(semanticSearch);
            log.info("Semantic search history created successfully");

        } catch (SQLException | IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemanticSearchResponse> getHistoriesByUser() {
        List<SemanticSearch> semanticSearches = semanticSearchRepository.findBySemanticSearchByUser(userService.getCurrentUser());
        if (semanticSearches.isEmpty()) {
            log.warn("No semantic search history found for the user");
            throw new ResourceNotFoundException(ErrorCode.HISTORY_NOT_EXISTED);
        }
        log.info("Found {} semantic search histories for the user", semanticSearches.size());
        List<SemanticSearchResponse> responses = new ArrayList<>();
        for (SemanticSearch semanticSearch : semanticSearches) {
            SemanticSearchResponse response = semanticSearchMapper.toSemanticSearchResponse(semanticSearch);
            response.setImageUrl(DEFAULT_ENDPOINT_URL + semanticSearch.getId());
            responses.add(response);
        }

        log.info("Generated {} semantic search responses", responses.size());
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getImageById(Long id) {
        SemanticSearch semanticSearch = semanticSearchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.HISTORY_NOT_EXISTED));
        log.info("Retrieving image for semantic search history with ID: {}", id);
        try {
            Blob imageBlob = semanticSearch.getImageBytes();
            return imageBlob.getBytes(1, (int) imageBlob.length());

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
