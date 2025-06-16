package vn.base.edumate.history.semantic;

import java.util.List;

public interface SemanticSearchService {

    void addNewSemanticSearchHistory(SemanticSearchRequest request);

    List<SemanticSearchResponse> getHistoriesByUser();

    byte[] getImageById(Long id);

    void deleteHistoryById(Long id);

    void deleteAllHistoriesByUser();
}
