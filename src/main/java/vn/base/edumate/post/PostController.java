package vn.base.edumate.post;

import java.awt.print.Pageable;
import java.util.LinkedHashSet;
import java.util.List;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.base.edumate.common.base.DataResponse;
import vn.base.edumate.common.base.PagedResponse;
import vn.base.edumate.common.util.TagType;

@Slf4j
@RestController
@RequestMapping("post")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    DataResponse<PostResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
        return DataResponse.<PostResponse>builder()
                .message("Thêm bài viết thành công!")
                .data(postService.savePost(request))
                .build();
    }
    @GetMapping
    DataResponse<List<PostResponse>> getAll(){
        return DataResponse.<List<PostResponse>>builder()
                .data(postService.getAll())
                .build();
    }

    @GetMapping("/tag/{id}")
    DataResponse<LinkedHashSet<PostResponse>> getPostByTag(@PathVariable("id") Long tagId) {
        return DataResponse.<LinkedHashSet<PostResponse>>builder()
                .message("Tìm thấy bài viết")
                .data(postService.getPostsByTag(tagId))
                .build();
    }

    @GetMapping("/tag/type/{type}")
    @PreAuthorize("hasRole('USER')")
    DataResponse<PagedResponse<PostResponse>> getPostsByTagType(@RequestParam int page,
                                                                @RequestParam int limit,
                                                                @PathVariable("type") TagType type) {
        PageRequest pageRequest = PageRequest.of(page-1,limit);
        Integer totalItem = postService.getPostCountByTagType(type);
        int totalPage = (int) Math.ceil((double)  totalItem/ limit);
        LinkedHashSet<PostResponse> linkedHashSetDataResponse = postService.getPostByTagType(pageRequest,type);
        return DataResponse.<PagedResponse<PostResponse>>builder()
                .message("Tìm thấy bài viết")
                .data(PagedResponse .<PostResponse>builder()
                                .content(linkedHashSetDataResponse)
                        .limit(limit)
                        .currentPage(page)
                        .totalPages(totalPage)
                        .totalElements(totalItem)
                        .build())
                .build();
    }
    @GetMapping("/by-user/{userId}")
    @PreAuthorize("hasRole('USER')")
    DataResponse<LinkedHashSet<PostResponse>> getPostsByUserId(@PathVariable("userId") String userId) {
        return  DataResponse.<LinkedHashSet<PostResponse>>builder()
                .message("success")
                .data(postService.getPostsByUserId(userId))
                .build();
    }
    @GetMapping("/by-user-like")
    DataResponse<LinkedHashSet<PostResponse>> getPostsByUserId() {
        return  DataResponse.<LinkedHashSet<PostResponse>>builder()
                .message("success")
                .data(postService.getPostByCurrentUserLike())
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

    @GetMapping("/{id}")
    DataResponse<PostResponse> getPostById(@PathVariable("id") Long id) {
        return DataResponse.<PostResponse>builder()
                .message("tìm bài viết thành công")
                .data(postService.getPostResponseById(id))
                .build();
    }
    @GetMapping("/admin/{id}")
    DataResponse<PostResponse> getPostByAdminId(@PathVariable("id") Long id) {
        return DataResponse.<PostResponse>builder()
                .message("tìm bài viết thành công")
                .data(postService.getPostByIdAdmin(id))
                .build();
    }

    @DeleteMapping("/{id}")
    DataResponse<Integer> deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return DataResponse.<Integer>builder()
                .message("Xóa bài viết thành công")
                .build();
    }

    @PostMapping("hide/{postId}")
    @PreAuthorize("hasRole('USER')")
    DataResponse<Void> hidePost(@PathVariable("postId") Long postId) {
        postService.hidePostForCurrentUser(postId);
        return DataResponse.<Void>builder().message("Ẩn bài viết thành công").build();
    }

    @GetMapping("/by-image/{id}")
    DataResponse<Long> getPostIdByImageId(@PathVariable("id") Long id) {
        log.info("Lấy ID bài viết từ ảnh với ID ảnh: {}", id);
        return DataResponse.<Long>builder()
                .message("Lấy ID bài viết thành công")
                .data(postService.getPostIdByImageId(id))
                .build();
    }
}
