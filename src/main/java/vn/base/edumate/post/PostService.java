package vn.base.edumate.post;



import java.util.List;

public interface PostService {
    PostResponse createPost(CreatePostRequest createPostRequest);
    List<PostResponse> getPostsByTag(Long tagId);
}
