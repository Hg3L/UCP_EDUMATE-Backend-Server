package vn.base.edumate.post_report;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.base.edumate.common.base.DataResponse;

@RestController
@RequestMapping("report")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PostReportController {
    PostReportService postReportService;

    @PostMapping("/{postId}")
    @PreAuthorize("hasRole('USER')")
    public DataResponse<Void> report(@PathVariable("postId") Long postId,@RequestBody ReportRequest reason){
        postReportService.reportPost(postId,reason);
        return DataResponse.<Void>builder()
                .message("Tố cáo bài viết thành công")
                .build();
    }
}
