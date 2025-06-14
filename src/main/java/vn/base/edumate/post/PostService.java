package vn.base.edumate.post;

import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import vn.base.edumate.common.util.TagType;

public interface PostService {
    Post getPostById(Long id);

    PostResponse savePost(CreatePostRequest createPostRequest);

    LinkedHashSet<PostResponse> getPostsByTag(Long tagId);

    LinkedHashSet<PostResponse> getPostByTagType(PageRequest pageRequest, TagType tagType);
    LinkedHashSet<PostResponse> getPostsByUserId(String userId);
    LinkedHashSet<PostResponse> getPostByCurrentUserLike();

    int likePost(Long postId);

    PostResponse getPostResponseById(Long id);

    Long getPostIdByImageId(Long id);

    void deletePost(Long id);

    void hidePostForCurrentUser(Long postId);

    List<Post> getPostsByIds(List<Long> postIds);
    Integer getPostCountByTagType(TagType tagType);
    List<PostResponse> getAll();
}
