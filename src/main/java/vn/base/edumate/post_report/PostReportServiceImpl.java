package vn.base.edumate.post_report;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.base.edumate.common.exception.BaseApplicationException;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.post.Post;
import vn.base.edumate.post.PostRepository;
import vn.base.edumate.user.entity.User;
import vn.base.edumate.user.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostReportServiceImpl implements PostReportService {
    UserService userService;
    PostRepository postRepository;
    PostReportRepository reportRepository;
    PostReportMapper postReportMapper;

    @Override
    public void reportPost(Long postId, ReportRequest reason) {
        User currentUser = userService.getCurrentUser();
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new BaseApplicationException(ErrorCode.POST_NOT_EXISTED));

        // Kiểm tra đã report chưa (tuỳ chọn)
        boolean exists = reportRepository.existsByUserAndPost(currentUser, post);
        if (exists) throw new BaseApplicationException(ErrorCode.REPORT_EXISTED_BY_USER);

        PostReport report = new PostReport();
        report.setUser(currentUser);
        report.setPost(post);
        report.setReason(reason.getReason());
        reportRepository.save(report);
    }

    @Override
    public List<PostReportResponse> getAll() {
        return reportRepository.findAll().stream().map(
                 postReportMapper::toPostReportResponse
         ).toList();
    }
}
