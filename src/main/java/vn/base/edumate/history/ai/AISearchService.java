package vn.base.edumate.history.ai;

import java.util.List;

public interface AISearchService {

    void addNewHistoryAISearch(AISearchRequest request);

    List<AISearchResponse> getHistoriesByUser();

    byte[] getImageById(Long id);

    void deleteHistoryById(Long id);

    void deleteAllHistoriesByUser();
}
