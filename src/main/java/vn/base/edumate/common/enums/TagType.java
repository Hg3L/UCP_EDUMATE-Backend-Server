package vn.base.edumate.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum TagType {
    SHARING_KNOWLEDGE("Chia sẻ kiến thức"),

    HOMEWORK_SUPPORT("Giải đáp bài tập");
    ;
    private final String description;

    TagType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static TagType fromDescription(String value) {
        for (TagType type : TagType.values()) {
            if (type.description.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("TAG_TYPE_NOT_EXISTED");
    }
}
