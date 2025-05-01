package vn.base.edumate.post;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.base.edumate.common.base.DataResponse;

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
}
