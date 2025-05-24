package vn.base.edumate.image;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.common.base.AbstractDTO;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageResponse extends AbstractDTO {
    private String imageUrl;
    private String fileName;
    private String fileType;
}
