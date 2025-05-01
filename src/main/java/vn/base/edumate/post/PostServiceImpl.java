package vn.base.edumate.post;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.base.edumate.common.exception.BaseApplicationException;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.tag.Tag;
import vn.base.edumate.tag.TagRepository;

import vn.base.edumate.user.entity.User;
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
    UserService userService;
    TagRepository tagRepository;
    @Override
    public PostResponse createPost(CreatePostRequest createPostRequest)  {
        Post post = postMapper.toModel(createPostRequest);
        User principal=  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserById(principal.getId());
        Tag tag =  tagRepository.findById(createPostRequest.getTagId())
                .orElseThrow( () -> new BaseApplicationException(ErrorCode.TAG_NOT_EXISTED));
        post.setTag(tag);
        post.setAuthor(user);

        return postMapper.toResponse(postRepository.save(post)) ;
    }

    @Override
    public List<PostResponse> getPostsByTag(Long tagId) {
        AtomicReference<List<PostResponse>> postsResponse = new AtomicReference<>(new ArrayList<>());
                postRepository.findByTagId(tagId).ifPresentOrElse(
                posts -> postsResponse.set(posts.stream().map(post -> {
                    PostResponse postResponse = postMapper.toResponse(post);
                    postResponse.setTagName(post.getTag().getName());
                    postResponse.setAuthorName(post.getAuthor().getUsername());
                    return postResponse;
                }).toList())
                ,() -> { throw new BaseApplicationException(ErrorCode.POST_NOT_EXISTED);}
        );
        return postsResponse.get();
    }
}
