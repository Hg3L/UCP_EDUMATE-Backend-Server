package vn.base.edumate.post;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.client.RestClient;
import vn.base.edumate.common.exception.BaseApplicationException;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.util.PostStatus;
import vn.base.edumate.common.util.TagType;
import vn.base.edumate.image.Image;
import vn.base.edumate.image.ImageRepository;
import vn.base.edumate.post_report.PostReportRepository;
import vn.base.edumate.postlike.PostLike;
import vn.base.edumate.postlike.PostLikeRepository;
import vn.base.edumate.tag.Tag;
import vn.base.edumate.tag.TagRepository;
import vn.base.edumate.user.entity.User;
import vn.base.edumate.user.repository.UserRepository;
import vn.base.edumate.user.service.UserService;
import vn.base.edumate.vision.DeleteImageRequest;

import static java.util.stream.Collectors.toList;


@Slf4j
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
    PostReportRepository postReportRepository;
    RestClient restClient;

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new BaseApplicationException(ErrorCode.POST_NOT_EXISTED));
    }

    @Override
    public PostResponse savePost(CreatePostRequest createPostRequest) {
        log.info("createPostRequest: {}", createPostRequest);
        if (createPostRequest.getId() != null) {
            Post post = getPostById(createPostRequest.getId());
            postMapper.updatePost(post, createPostRequest);
            if (createPostRequest.getImageIds() != null) {
                List<Long> imageIds = createPostRequest.getImageIds();
                List<Image> images = imageRepository.findAllById(imageIds);
                log.info("images: {}", images.stream().map(Image::getId).collect(toList()));
                post.setImages(images);
            } else {
                log.info("images: null");
                post.setImages(null);
            }
            return postMapper.toResponse(postRepository.save(post));
        } else {
            log.info("createPostRequest.getId() == null -> create post");
            log.info("createPostRequest: {}", createPostRequest);
            Post post = postMapper.toModel(createPostRequest);
            User user = userService.getCurrentUser();
            Tag tag = tagRepository
                    .findById(createPostRequest.getTagId())
                    .orElseThrow(() -> new BaseApplicationException(ErrorCode.TAG_NOT_EXISTED));
            if (createPostRequest.getImageIds() != null) {
                List<Long> imageIds = createPostRequest.getImageIds();
                List<Image> images = imageRepository.findAllById(imageIds);
                log.info("images: {}", images.stream().map(Image::getId).toList());
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
    public LinkedHashSet<PostResponse> getPostsByTag(Long tagId) {
        AtomicReference<LinkedHashSet<PostResponse>> postsResponse = new AtomicReference<>(new LinkedHashSet<>());
        User user = userService.getCurrentUser();
        postRepository
                .findByTagIdAndStatus(tagId,PostStatus.ACTIVE)
                .filter(list -> !list.isEmpty())
                .ifPresentOrElse(
                        posts -> postsResponse.set(posts.stream()
                                .map(post -> {
                                    PostResponse postResponse =  postMapper.toResponse(post,user,postLikeRepository);
                                    postResponse.setCommentCount(
                                            post.getComments().size());
                                    return postResponse;
                                })
                                .collect(Collectors.toCollection(LinkedHashSet::new))),
                        () -> {
                            throw new BaseApplicationException(ErrorCode.POST_NOT_EXISTED);
                        });
        return postsResponse.get();
    }

    @Override
    public LinkedHashSet<PostResponse> getPostByTagType(PageRequest pageRequest, TagType tagType) {
        User user = userService.getCurrentUser();
        List<Post> hiddenPosts = user.getHiddenPosts(); // lấy danh sách post bị ẩn

        AtomicReference<LinkedHashSet<PostResponse>> postsResponse = new AtomicReference<>(new LinkedHashSet<>());

        postRepository
                .findByTagTagTypeAndStatusOrderByCreatedAtDesc( pageRequest,tagType, PostStatus.ACTIVE)
                .filter(list -> !list.isEmpty())
                .ifPresentOrElse(
                        posts -> {
                            LinkedHashSet<PostResponse> visiblePosts = posts.stream()
                                    .filter(post -> !hiddenPosts.contains(post)) // lọc bài viết đã bị ẩn
                                    .map(post -> {
                                        PostResponse postResponse =  postMapper.toResponse(post,user,postLikeRepository);
                                        postResponse.setCommentCount(post.getComments().stream()
                                                .filter(comment -> comment.getParent() == null)
                                                .collect(Collectors.toCollection(LinkedHashSet::new))
                                                .size());
                                        return postResponse;
                                    })
                                    .collect(Collectors.toCollection(LinkedHashSet::new));
                            postsResponse.set(visiblePosts);
                        },
                        () -> {
                            throw new BaseApplicationException(ErrorCode.POST_NOT_EXISTED);
                        });

        return postsResponse.get();
    }

    @Override
    public LinkedHashSet<PostResponse> getPostsByUserId(String userId) {
        AtomicReference<LinkedHashSet<PostResponse>> postsResponse = new AtomicReference<>(new LinkedHashSet<>());
        User user = userService.getUserById(userId);
        postRepository.findByAuthorIdAndStatus(userId,PostStatus.ACTIVE).ifPresentOrElse(posts -> {
            postsResponse.set(posts.stream().map( post -> {
                PostResponse postResponse =  postMapper.toResponse(post,user,postLikeRepository);
                postResponse.setCommentCount(post.getComments().stream()
                        .filter(comment -> comment.getParent() == null)
                        .toList()
                        .size());
                return postResponse;
            })
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        },() -> {throw new BaseApplicationException(ErrorCode.POST_NOT_EXISTED);});
        return postsResponse.get();
    }

    @Override
    public LinkedHashSet<PostResponse> getPostByCurrentUserLike() {
        User user = userService.getCurrentUser();
        AtomicReference<LinkedHashSet<PostResponse>> postsResponse = new AtomicReference<>(new LinkedHashSet<>());
        Optional.ofNullable(user.getPostLikes()).ifPresentOrElse(posts -> {
            List<Post> postList = posts.stream()
                    .map(PostLike::getPost)
                    .filter(post -> post.getStatus() == PostStatus.ACTIVE)
                    .toList();

            postsResponse.set(postList.stream().map(post -> {
                 PostResponse postResponse = postMapper.toResponse(post,user,postLikeRepository);
                 postResponse.setCommentCount(post.getComments().stream()
                         .filter(comment -> comment.getParent() == null)
                         .toList()
                         .size());
                 return postResponse;
             }).collect(Collectors.toCollection(LinkedHashSet::new)));
        },() -> {throw new BaseApplicationException(ErrorCode.POST_NOT_EXISTED);});
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
        User user = userService.getCurrentUser();
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new BaseApplicationException(ErrorCode.POST_NOT_EXISTED));
        PostResponse postResponse =  postMapper.toResponse(post,user,postLikeRepository);
        postResponse.setCommentCount(post.getComments().size());
        postResponse.setReportCount(postReportRepository.countByPost(post));

        return postResponse;
    }

    @Override
    public Long getPostIdByImageId(Long id) {
        return postRepository.findByImages_Id(id)
                .orElseThrow(() -> new BaseApplicationException(ErrorCode.POST_NOT_EXISTED))
                .getId();
    }

    @Override
    public void deletePost(Long id) {

        List<Long> imageIds = postRepository.findById(id)
                .orElseThrow(() -> new BaseApplicationException(ErrorCode.POST_NOT_EXISTED))
                .getImages()
                .stream()
                .map(Image::getId)
                .toList();

        if (!imageIds.isEmpty()) {
            restClient.method(HttpMethod.DELETE)
                    .uri("http://localhost:8888/api/v1/delete")
                    .body(DeleteImageRequest.builder().ids(imageIds).build())
                    .retrieve()
                    .body(Map.class);

        }

        postRepository.deleteById(id);
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

    @Override
    public List<Post> getPostsByIds(List<Long> postIds) {
        List<Post> posts = new ArrayList<>();
        log.info("postIds: {}", postIds);
        if (postIds != null && !postIds.isEmpty()) {
            log.info("postIds.size() > 0");
            posts = postRepository.findAllById(postIds);
        }
        else {
            log.info("ids size = 0, return empty list");
        }
        return posts;
    }

    @Override
    public Integer getPostCountByTagType(TagType tagType) {
        return postRepository.countByTagTagTypeAndStatus(tagType,PostStatus.ACTIVE);
    }

    @Override
    public List<PostResponse> getAll() {
        AtomicReference<List<PostResponse>> postsResponse = new AtomicReference<>(new ArrayList<>());
         postRepository.findAllByStatus(PostStatus.ACTIVE).ifPresentOrElse(posts -> {
            List<PostResponse> postResponses = new ArrayList<>();
            postsResponse.set(posts.stream().map(post ->{
                PostResponse postResponse = postMapper.toResponse(post);
                postResponse.setCommentCount(post.getComments().stream()
                        .filter(comment -> comment.getParent() == null)
                        .toList()
                        .size());
                postResponse.setReportCount(postReportRepository.countByPost(post));

                return postResponse;
            }).toList());
        },() -> {throw new BaseApplicationException(ErrorCode.POST_NOT_EXISTED);});
         return postsResponse.get();
    }
}
