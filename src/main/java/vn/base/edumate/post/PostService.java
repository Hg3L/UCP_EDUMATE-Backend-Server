package vn.base.edumate.post;

import java.util.List;

import vn.base.edumate.common.util.TagType;

public interface PostService {
    Post getPostById(Long id);

    PostResponse savePost(CreatePostRequest createPostRequest);

    List<PostResponse> getPostsByTag(Long tagId);

    List<PostResponse> getPostByTagType(TagType tagType);
    List<PostResponse> getPostsByUserId(String userId);
    List<PostResponse> getPostByCurrentUserLike();

    int likePost(Long postId);

    PostResponse getPostResponseById(Long id);


    void deletePost(Long id);

    void hidePostForCurrentUser(Long postId);
}
