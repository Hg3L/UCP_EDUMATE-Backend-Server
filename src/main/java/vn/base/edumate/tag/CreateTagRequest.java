package vn.base.edumate.tag;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import vn.base.edumate.common.enums.TagType;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTagRequest {
    String name;
    TagType tagType;
}
