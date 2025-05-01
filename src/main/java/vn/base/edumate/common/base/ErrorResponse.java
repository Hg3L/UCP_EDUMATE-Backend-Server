package vn.base.edumate.common.base;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {
    @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
    LocalDateTime timestamp;
    int code;
    int status;
    String path;
    String message;
    List<ErrorDetail> details;
}
