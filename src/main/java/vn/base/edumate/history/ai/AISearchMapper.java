package vn.base.edumate.history.ai;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AISearchMapper {
    AISearchResponse toAISearchResponse(AISearch aiSearch);

    AISearch toAISearch(AISearchRequest aiSearchRequest);
}
