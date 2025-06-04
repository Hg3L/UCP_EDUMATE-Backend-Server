package vn.base.edumate.history.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.base.edumate.common.exception.BaseApplicationException;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.exception.ResourceNotFoundException;
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
public class AISearchServiceImp implements AISearchService {

    private static final String DEFAULT_ENDPOINT_URL = "/history/ai-search/image/";

    private final AISearchRepository aiSearchRepository;
    private final UserService userService;
    private final AISearchMapper mapper;

    @Transactional
    @Override
    public void addNewHistoryAISearch(AISearchRequest request) {

        try {
            AISearch aiSearch = AISearch.builder()
                    .user(userService.getUserById(request.getUid()))
                    .imageBytes(new SerialBlob(request.getImage().getBytes()))
                    .answer(request.getAnswer())
                    .build();
            aiSearchRepository.save(aiSearch);
            log.info("History AI Search created successfully");
        } catch (SQLException | IOException e) {
            throw new BaseApplicationException(ErrorCode.UNCATEGORIZED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AISearchResponse> getHistoriesByUser() {
        List<AISearch> aiSearches = aiSearchRepository.findAISearchByUser(userService.getCurrentUser());
        if (aiSearches.isEmpty()) {
            log.warn("No AI search history found for the user");
            throw new ResourceNotFoundException(ErrorCode.HISTORY_NOT_EXISTED);
        }
        log.info("Found {} AI search history for the user", aiSearches.size());
        List<AISearchResponse> responses = new ArrayList<>();
        for (AISearch aiSearch : aiSearches) {
            AISearchResponse aiSearchResponse = mapper.toAISearchResponse(aiSearch);
            aiSearchResponse.setImageUrl(DEFAULT_ENDPOINT_URL + aiSearch.getId());
            log.info("Link: {}", aiSearchResponse.getImageUrl());
            responses.add(aiSearchResponse);
        }
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getImageById(Long id) {
        AISearch aiSearch = aiSearchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.HISTORY_NOT_EXISTED));
        log.info("Retrieving image for AI search history with ID: {}", id);
        try {
            Blob imageBlob = aiSearch.getImageBytes();
            return imageBlob.getBytes(1, (int) imageBlob.length());

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
