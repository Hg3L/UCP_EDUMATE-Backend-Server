package vn.base.edumate.common.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class ErrorResponse {
    @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
    LocalDateTime timestamp;
    int status;
    String path;
    String message;
    List<ErrorDetail> details;
}
