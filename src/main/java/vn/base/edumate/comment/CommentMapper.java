package vn.base.edumate.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toModel(CreateCommentRequest createCommentRequest) ;
    @Mapping(target = "repliesCount", expression = "java(comment.getChildren() != null ? comment.getChildren().size() : 0)")
    CommentResponse toResponse(Comment comment) ;
}
