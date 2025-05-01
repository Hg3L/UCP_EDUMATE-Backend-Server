package vn.base.edumate.tag;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import vn.base.edumate.common.base.AbstractDTO;
import vn.base.edumate.common.util.TagType;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagResponse extends AbstractDTO {
    String name;
    TagType tagType;
}
