package vn.base.edumate.comment;

import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import vn.base.edumate.commentlike.CommentLikeRepository;
import vn.base.edumate.user.entity.User;

@Mapper(componentModel = "spring")

public interface CommentMapper {

    Comment toModel(CreateCommentRequest createCommentRequest);

    @Named("simpleResponse")
    @Mapping(
            target = "repliesCount",
            expression = "java(comment.getChildren() != null ? comment.getChildren().size() : 0)")
    CommentResponse toResponse(Comment comment);
    @Mapping(
            target = "repliesCount",
            expression = "java(comment.getChildren() != null ? comment.getChildren().size() : 0)")
    @Mapping(target = "parent", qualifiedByName = "simpleResponse")
    CommentResponse toResponse(Comment comment, @Context User user, @Context CommentLikeRepository commentLikeRepository);

    @AfterMapping
    default void setLiked(@MappingTarget CommentResponse response,
                          Comment comment,
                          @Context User user,
                          @Context CommentLikeRepository commentLikeRepository) {
        response.setLiked(commentLikeRepository.existsByUserAndComment(user, comment));
    }
}
