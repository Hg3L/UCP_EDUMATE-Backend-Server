package vn.base.edumate.post;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.base.edumate.common.base.DataResponse;
import vn.base.edumate.common.util.TagType;

import java.util.List;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PostController {
    PostService postService;
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    DataResponse<PostResponse> createPost(@RequestBody CreatePostRequest request) {
        return DataResponse.<PostResponse>builder()
                .message("Thêm bài viết thành công!")
                .data(postService.createPost(request))
                .build();
    }

    @GetMapping("/tag/{id}")
    DataResponse<List<PostResponse>> getPost(@PathVariable("id") Long tagId) {
        return DataResponse.<List<PostResponse>>builder()
                .message("Tìm thấy bài viết")
                .data(postService.getPostsByTag(tagId))
                .build();
    }
    @GetMapping("/tag/type/{type}")
    DataResponse<List<PostResponse>> getPostsByTagType(@PathVariable("type") TagType type) {
        return DataResponse.<List<PostResponse>>builder()
                .message("Tìm thấy bài viết")
                .data(postService.getPostByTagType(type))
                .build();
    }
    @PutMapping("like/{postId}")
    @PreAuthorize("hasRole('USER')")
    DataResponse<Integer> likePost(@PathVariable("postId") Long postId) {
        return DataResponse.<Integer>builder()
                .message("Like bài viết thành công")
                .data(postService.likePost(postId))
                .build();
    }
}
