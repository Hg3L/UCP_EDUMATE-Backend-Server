package vn.base.edumate.common.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedHashSet;

@Data
@Builder
public class PagedResponse<T> {
    private LinkedHashSet<T> content;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int limit;
}
