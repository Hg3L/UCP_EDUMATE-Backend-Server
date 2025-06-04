package vn.base.edumate.history.semantic;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SemanticSearchMapper {

    SemanticSearchResponse toSemanticSearchResponse(SemanticSearch semanticSearch);

    SemanticSearch toSemanticSearch(SemanticSearchRequest semanticSearchRequest);
}
