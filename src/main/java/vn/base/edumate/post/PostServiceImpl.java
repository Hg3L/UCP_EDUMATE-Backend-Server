package vn.base.edumate.post;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.base.edumate.common.exception.BaseApplicationException;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.util.TagType;
import vn.base.edumate.tag.Tag;
import vn.base.edumate.tag.TagRepository;

import vn.base.edumate.user.entity.User;
import vn.base.edumate.user.repository.UserRepository;
import vn.base.edumate.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    UserRepository userRepository;
    UserService userService;
    TagRepository tagRepository;

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new BaseApplicationException(ErrorCode.POST_NOT_EXISTED));
    }

    @Override
    public PostResponse createPost(CreatePostRequest createPostRequest)  {
        Post post = postMapper.toModel(createPostRequest);
        User user = userService.getCurrentUser();
        Tag tag =  tagRepository.findById(createPostRequest.getTagId())
                .orElseThrow( () -> new BaseApplicationException(ErrorCode.TAG_NOT_EXISTED));
        post.setTag(tag);
        post.setAuthor(user);

        return postMapper.toResponse(postRepository.save(post)) ;
    }

    @Override
    public List<PostResponse> getPostsByTag(Long tagId) {
        AtomicReference<List<PostResponse>> postsResponse = new AtomicReference<>(new ArrayList<>());
                postRepository.findByTagId(tagId)
                        .filter(list -> !list.isEmpty())
                        .ifPresentOrElse(
                posts -> postsResponse.set(posts.stream()
                        .map(postMapper::toResponse)
                        .toList())
                ,() -> { throw new BaseApplicationException(ErrorCode.POST_NOT_EXISTED);}
        );
        return postsResponse.get();
    }

    @Override
    public List<PostResponse> getPostByTagType(TagType tagType) {
        AtomicReference<List<PostResponse>> postsResponse = new AtomicReference<>(new ArrayList<>());
        postRepository.findByTagTagType(tagType)
                .filter(list -> !list.isEmpty())
                .ifPresentOrElse(
                posts -> postsResponse.set(posts.stream()
                        .map(postMapper::toResponse)
                        .toList())
                ,() -> { throw new BaseApplicationException(ErrorCode.POST_NOT_EXISTED);}
        );
        return postsResponse.get();
    }

    @Override
    public int likePost(Long postId) {
        User user = userService.getCurrentUser();
        Post post = getPostById(postId);
        if(user.getPostsLike().contains(post)) {
            user.getPostsLike().remove(post);
            userRepository.save(user);
            post.setLikeCount(post.getLikeCount() -1);
        }
        else{
            user.getPostsLike().add(post);
            userRepository.save(user);
            post.setLikeCount(post.getLikeCount() + 1);
        }
        return postRepository.save(post).getLikeCount();
    }
}
