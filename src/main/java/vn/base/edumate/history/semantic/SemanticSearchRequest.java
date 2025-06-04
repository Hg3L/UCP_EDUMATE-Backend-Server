package vn.base.edumate.history.semantic;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SemanticSearchRequest {
    String uid;
    MultipartFile image;
    List<Long> imgIds = new ArrayList<>();
}
