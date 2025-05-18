package vn.base.edumate.post;



import vn.base.edumate.common.util.TagType;

import java.util.List;

public interface PostService {
    Post getPostById(Long id);
    PostResponse savePost(CreatePostRequest createPostRequest);
    List<PostResponse> getPostsByTag(Long tagId);
    List<PostResponse> getPostByTagType(TagType tagType);
    int likePost(Long postId) ;
    PostResponse getPostResponseById(Long id);
}
