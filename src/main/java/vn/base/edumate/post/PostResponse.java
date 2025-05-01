package vn.base.edumate.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.common.base.AbstractDTO;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse extends AbstractDTO {
    private String content;
    private String title;
    private String authorName;
    private String tagName;
    private List<String> imageUrls = new ArrayList<>();
}
