package vn.base.edumate.history.ai;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.common.base.AbstractDTO;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AISearchResponse extends AbstractDTO {
    private String answer;
    private String imageUrl;
}
