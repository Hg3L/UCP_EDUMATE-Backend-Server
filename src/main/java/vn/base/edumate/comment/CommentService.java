package vn.base.edumate.comment;

import java.util.List;

public interface CommentService {
    int Like(Long commentId);

    CommentResponse createComment(Long postId, CreateCommentRequest createCommentRequest);

    List<CommentResponse> getCommentsByPostId(Long postId);

    CommentResponse getCommentById(Long commentId);

    CommentResponse createChildComment(CreateCommentRequest createCommentRequest, Long parentCommentId);

    List<CommentResponse> getCommentsByParentCommentId(Long parentCommentId);
    List<CommentResponse> getCommentsAndRepliesByCurrentUser();
    List<CommentResponse> getAll();
}
