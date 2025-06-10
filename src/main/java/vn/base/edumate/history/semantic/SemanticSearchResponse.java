package vn.base.edumate.history.semantic;

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
public class SemanticSearchResponse extends AbstractDTO {

    private String imageUrl;

}
