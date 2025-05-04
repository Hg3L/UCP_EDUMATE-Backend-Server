package vn.base.edumate.comment;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.base.edumate.user.entity.User;
import vn.base.edumate.user.service.UserService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CommentSerivceImpl implements CommentService {
    CommentRepository commentRepository;
    UserService userService;
    @Override
    public int Like(Long commentId, String userId) {
        User user = userService.getUserById(userId);
        return 0;
    }
}
