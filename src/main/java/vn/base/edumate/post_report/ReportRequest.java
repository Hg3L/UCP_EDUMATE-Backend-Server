package vn.base.edumate.post_report;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReportRequest {
    private String reason;
}
