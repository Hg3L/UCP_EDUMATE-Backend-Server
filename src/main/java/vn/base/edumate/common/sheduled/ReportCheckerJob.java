package vn.base.edumate.common.sheduled;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.base.edumate.common.util.PostStatus;
import vn.base.edumate.post.Post;
import vn.base.edumate.post.PostRepository;
import vn.base.edumate.post_report.PostReportRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ReportCheckerJob {
    PostRepository postRepository;
    PostReportRepository postReportRepository;

    @Scheduled(fixedRate = 300_000) // mỗi 5 phút
    public void checkReportedPosts() {
        List<Post> allPosts = postRepository.findAll();

        for (Post post : allPosts) {
            long reportCount = postReportRepository.countByPost(post);
            if (reportCount == 5 && !post.getStatus().equals(PostStatus.REPORTED)) {
                post.setStatus(PostStatus.REPORTED); // hoặc HIDDEN
                postRepository.save(post);
            }
            else if (reportCount > 5 && !post.getStatus().equals(PostStatus.HIDDEN)) {
                post.setStatus(PostStatus.HIDDEN);
                postRepository.save(post);
            }
        }
    }
}
