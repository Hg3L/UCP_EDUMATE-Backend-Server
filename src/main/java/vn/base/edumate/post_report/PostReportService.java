package vn.base.edumate.post_report;

import java.util.List;

public interface PostReportService {
    public void reportPost(Long postId, ReportRequest reason);
    List<PostReportResponse> getAll();
}
