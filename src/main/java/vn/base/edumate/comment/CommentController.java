package vn.base.edumate.comment;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.base.edumate.common.base.DataResponse;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentService commentService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public DataResponse<CommentResponse> createComment(
            @RequestParam Long postId,@Valid @RequestBody CreateCommentRequest request) {
        return DataResponse.<CommentResponse>builder()
                .message("đăng bình luận thành công")
                .data(commentService.createComment(postId, request))
                .build();
    }

    @GetMapping("/post/{postId}")
    public DataResponse<List<CommentResponse>> getCommentByPostId(@PathVariable("postId") Long postId) {
        return DataResponse.<List<CommentResponse>>builder()
                .data(commentService.getCommentsByPostId(postId))
                .message("tìm thành công")
                .build();
    }

    @PutMapping("like/{commentId}")
    @PreAuthorize("hasRole('USER')")
    DataResponse<Integer> likePost(@PathVariable("commentId") Long commentId) {
        return DataResponse.<Integer>builder()
                .message("Like bình luận thành công")
                .data(commentService.Like(commentId))
                .build();
    }

    @GetMapping("/{commentId}")
    public DataResponse<CommentResponse> getCommentById(@PathVariable("commentId") Long commentId) {
        return DataResponse.<CommentResponse>builder()
                .message("success")
                .data(commentService.getCommentById(commentId))
                .build();
    }
    @GetMapping("by-current-user")
    @PreAuthorize("hasRole('USER')")
    public DataResponse<List<CommentResponse>> getCommentById() {
        return DataResponse.<List<CommentResponse>>builder()
                .message("success")
                .data(commentService.getCommentsAndRepliesByCurrentUser())
                .build();
    }

    @PostMapping("/{parentId}/replies")
    @PreAuthorize("hasRole('USER')")
    public DataResponse<CommentResponse> saveReply(
            @PathVariable Long parentId, @RequestBody @Valid CreateCommentRequest createCommentRequest) {
        return DataResponse.<CommentResponse>builder()
                .data(commentService.createChildComment(createCommentRequest, parentId))
                .message("success")
                .build();
    }

    @GetMapping("/{parentId}/replies")
    public DataResponse<List<CommentResponse>> getReplies(@PathVariable Long parentId) {
        return DataResponse.<List<CommentResponse>>builder()
                .data(commentService.getCommentsByParentCommentId(parentId))
                .message("success")
                .build();
    }
    @GetMapping
    public DataResponse<List<CommentResponse>> getAll(){
        return DataResponse.<List<CommentResponse>>builder()
                .data(commentService.getAll())
                .message("success")
                .build();
    }
}
