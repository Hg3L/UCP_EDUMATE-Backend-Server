package vn.base.edumate.common.base;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataResponse<T> {
    @Builder.Default
    private int status = 1000;

    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
}
