package vn.base.edumate.common.base;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractDTO {
    long id;
    String createdBy;
    String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/M/yyyy")
    Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/M/yyyy")
    Date updatedAt;
}
