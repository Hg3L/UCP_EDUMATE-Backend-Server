package vn.base.edumate.post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.base.edumate.common.exception.BaseApplicationException;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.util.PostStatus;
import vn.base.edumate.common.util.TagType;
import vn.base.edumate.image.Image;
import vn.base.edumate.image.ImageRepository;
import vn.base.edumate.postlike.PostLike;
import vn.base.edumate.postlike.PostLikeRepository;
import vn.base.edumate.tag.Tag;
import vn.base.edumate.tag.TagRepository;
import vn.base.edumate.user.entity.User;
import vn.base.edumate.user.repository.UserRepository;
import vn.base.edumate.user.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    UserRepository userRepository;
    ImageRepository imageRepository;
    UserService userService;
    TagRepository tagRepository;
    PostLikeRepository postLikeRepository;

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new BaseApplicationException(ErrorCode.POST_NOT_EXISTED));
    }

    @Override
    public PostResponse savePost(CreatePostRequest createPostRequest) {
        if (createPostRequest.getId() != null) {
            Post post = getPostById(createPostRequest.getId());
            postMapper.updatePost(post, createPostRequest);
            if (createPostRequest.getImageIds() != null) {
                List<Long> imageIds = createPostRequest.getImageIds();
                List<Image> images = imageRepository.findAllById(imageIds);
                post.setImages(images);
            } else {
                post.setImages(null);
            }
            return postMapper.toResponse(postRepository.save(post));
        } else {

            Post post = postMapper.toModel(createPostRequest);
            User user = userService.getCurrentUser();
            Tag tag = tagRepository
                    .findById(createPostRequest.getTagId())
                    .orElseThrow(() -> new BaseApplicationException(ErrorCode.TAG_NOT_EXISTED));
            if (createPostRequest.getImageIds() != null) {
                List<Long> imageIds = createPostRequest.getImageIds();
                List<Image> images = imageRepository.findAllById(imageIds);
                post.setImages(images);
            }
            post.setLikeCount(0);
            post.setTag(tag);
            post.setAuthor(user);
            post.setStatus(PostStatus.ACTIVE);
            PostResponse postResponse = postMapper.toResponse(postRepository.save(post));

            postResponse.setCommentCount(
                    post.getComments() == null ? 0 : post.getComments().size());
            return postResponse;
        }
    }

    @Override
    public List<PostResponse> getPostsByTag(Long tagId) {
        AtomicReference<List<PostResponse>> postsResponse = new AtomicReference<>(new ArrayList<>());
        postRepository
                .findByTagId(tagId)
                .filter(list -> !list.isEmpty())
                .ifPresentOrElse(
                        posts -> postsResponse.set(posts.stream()
                                .map(post -> {
                                    PostResponse postResponse = postMapper.toResponse(postRepository.save(post));
                                    postResponse.setCommentCount(
                                            post.getComments().size());
                                    return postResponse;
                                })
                                .toList()),
                        () -> {
                            throw new BaseApplicationException(ErrorCode.POST_NOT_EXISTED);
                        });
        return postsResponse.get();
    }

    @Override
    public List<PostResponse> getPostByTagType(TagType tagType) {
        User user = userService.getCurrentUser();
        List<Post> hiddenPosts = user.getHiddenPosts(); // lấy danh sách post bị ẩn

        AtomicReference<List<PostResponse>> postsResponse = new AtomicReference<>(new ArrayList<>());

        postRepository
                .findByTagTagTypeAndStatus(tagType, PostStatus.ACTIVE)
                .filter(list -> !list.isEmpty())
                .ifPresentOrElse(
                        posts -> {
                            List<PostResponse> visiblePosts = posts.stream()
                                    .filter(post -> !hiddenPosts.contains(post)) // lọc bài viết đã bị ẩn
                                    .map(post -> {
                                        PostResponse postResponse = postMapper.toResponse(post);
                                        postResponse.setCommentCount(post.getComments().stream()
                                                .filter(comment -> comment.getParent() == null)
                                                .toList()
                                                .size());
                                        return postResponse;
                                    })
                                    .toList();
                            postsResponse.set(visiblePosts);
                        },
                        () -> {
                            throw new BaseApplicationException(ErrorCode.POST_NOT_EXISTED);
                        });

        return postsResponse.get();
    }

    @Override
    public int likePost(Long postId) {
        User user = userService.getCurrentUser();
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new BaseApplicationException(ErrorCode.POST_NOT_EXISTED));

        Optional<PostLike> existing = postLikeRepository.findByUserAndPost(user, post);

        if (existing.isPresent()) {
            postLikeRepository.delete(existing.get());
            post.setLikeCount(post.getLikeCount() - 1);
        } else {
            PostLike like = PostLike.builder().user(user).post(post).build();
            postLikeRepository.save(like);
            post.setLikeCount(post.getLikeCount() + 1);
        }
        postRepository.save(post);
        return post.getLikeCount();
    }

    @Override
    public PostResponse getPostResponseById(Long id) {
        return postMapper.toResponse(postRepository
                .findById(id)
                .orElseThrow(() -> new BaseApplicationException(ErrorCode.POST_NOT_EXISTED)));
    }

    @Override
    public void deletePost(Long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }

    @Override
    public void hidePostForCurrentUser(Long postId) {
        User user = userService.getCurrentUser();
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new BaseApplicationException(ErrorCode.POST_NOT_EXISTED));

        if (!user.getHiddenPosts().contains(post)) {
            user.getHiddenPosts().add(post);
            userRepository.save(user);
        }
    }
}
