package vn.base.edumate.comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.base.edumate.commentlike.CommentLike;
import vn.base.edumate.commentlike.CommentLikeRepository;
import vn.base.edumate.common.exception.BaseApplicationException;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.image.Image;
import vn.base.edumate.image.ImageRepository;
import vn.base.edumate.post.Post;
import vn.base.edumate.post.PostService;
import vn.base.edumate.user.entity.User;
import vn.base.edumate.user.repository.UserRepository;
import vn.base.edumate.user.service.UserService;

import javax.swing.text.html.Option;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    UserService userService;
    UserRepository userRepository;
    PostService postService;
    ImageRepository imageRepository;
    CommentLikeRepository commentLikeRepository;
    CommentMapper commentMapper;

    @Override
    public int Like(Long commentId) {
        User user = userService.getCurrentUser();
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new BaseApplicationException(ErrorCode.COMMENT_NOT_EXISTED));

        Optional<CommentLike> existingLike = commentLikeRepository.findByUserAndComment(user, comment);

        if (existingLike.isPresent()) {
            // User đã like → unlike
            commentLikeRepository.delete(existingLike.get());
            comment.setLikes(comment.getLikes() - 1);
        } else {
            // Chưa like → thêm like
            CommentLike newLike =
                    CommentLike.builder().user(user).comment(comment).build();
            commentLikeRepository.save(newLike);
            comment.setLikes(comment.getLikes() + 1);
        }

        commentRepository.save(comment);
        return comment.getLikes();
    }

    @Override
    public CommentResponse createComment(Long postId, CreateCommentRequest createCommentRequest) {
        User user = userService.getCurrentUser();
        Post post = postService.getPostById(postId);
        Comment comment = commentMapper.toModel(createCommentRequest);
        if (createCommentRequest.getImageId() != null) {
            Image image =
                    imageRepository.findById(createCommentRequest.getImageId()).orElse(null);
            comment.setImage(image);
        }
        comment.setUser(user);
        comment.setPost(post);
        return commentMapper.toResponse(commentRepository.save(comment));
    }

    @Override
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        AtomicReference<List<CommentResponse>> commentsResponse = new AtomicReference<>(new ArrayList<>());
        User user = userService.getCurrentUser();
        commentRepository
                .findByPostId(postId)
                .ifPresentOrElse(
                        comments -> commentsResponse.set(
                                comments.stream().map(comment -> {
                                    return commentMapper.toResponse(comment,user,commentLikeRepository);
                                }).toList()),
                        () -> {
                            throw new BaseApplicationException(ErrorCode.COMMENT_NOT_EXISTED);
                        });
        return commentsResponse.get();
    }

    @Override
    public CommentResponse getCommentById(Long commentId) {
        User user = userService.getCurrentUser();
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new BaseApplicationException(ErrorCode.COMMENT_NOT_EXISTED));
        return commentMapper.toResponse(comment,user,commentLikeRepository);
    }

    @Override
    @Transactional
    public CommentResponse createChildComment(CreateCommentRequest createCommentRequest, Long parentCommentId) {
        User user = userService.getCurrentUser();
        Comment comment = commentMapper.toModel(createCommentRequest);

        Comment parent = commentRepository
                .findById(parentCommentId)
                .orElseThrow(() -> new BaseApplicationException(ErrorCode.COMMENT_NOT_EXISTED));
        Post post = postService.getPostById(parent.getPost().getId());
        comment.setParent(parent);
        comment.setPost(post);
        comment.setUser(user);
        if (createCommentRequest.getImageId() != null) {
            Image image = imageRepository
                    .findById(createCommentRequest.getImageId())
                    .orElseThrow(() -> new BaseApplicationException(ErrorCode.IMAGE_NOT_EXISTED));
            comment.setImage(image);
        }
        return commentMapper.toResponse(commentRepository.save(comment));
    }

    @Override
    public List<CommentResponse> getCommentsByParentCommentId(Long parentCommentId) {
        AtomicReference<List<CommentResponse>> commentsResponse = new AtomicReference<>(new ArrayList<>());
        User user = userService.getCurrentUser();
        commentRepository
                .findByParentId(parentCommentId)
                .ifPresentOrElse(
                        comments -> commentsResponse.set(
                                comments.stream().map(comment -> {
                                    return commentMapper.toResponse(comment,user,commentLikeRepository);
                                }).toList()),
                        () -> {
                            throw new BaseApplicationException(ErrorCode.COMMENT_NOT_EXISTED);
                        });
        return commentsResponse.get();
    }

    @Override
    public List<CommentResponse> getCommentsAndRepliesByCurrentUser() {
        User user = userService.getCurrentUser();
        String userId = user.getId();
        AtomicReference<List<CommentResponse>> commentsResponse = new AtomicReference<>(new ArrayList<>());
        commentRepository
                .findByUserId(userId)
                .ifPresentOrElse(
                        comments -> commentsResponse.set(
                                comments.stream().map(commentMapper::toResponse).toList()),
                        () -> {
                            throw new BaseApplicationException(ErrorCode.COMMENT_NOT_EXISTED);
                        });
        return commentsResponse.get();
    }

    @Override
    public List<CommentResponse> getAll() {
        AtomicReference<List<CommentResponse>> commentsResponse = new AtomicReference<>(new ArrayList<>());
        Optional.of(commentRepository.findAll())
                .ifPresentOrElse(
                        comments -> commentsResponse.set(
                                comments.stream().map(commentMapper::toResponse).toList()),
                        () -> {
                            throw new BaseApplicationException(ErrorCode.COMMENT_NOT_EXISTED);
                        });
        return commentsResponse.get();
    }
}
