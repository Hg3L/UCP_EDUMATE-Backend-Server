package vn.base.edumate.history.ai;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AISearchRequest {
    String uid;
    MultipartFile image;
    String answer;
}
