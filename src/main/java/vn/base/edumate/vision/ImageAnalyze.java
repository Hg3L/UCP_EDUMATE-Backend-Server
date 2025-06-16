package vn.base.edumate.vision;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ImageAnalyze {

    private Long id;

    private String content;

    private String imageUrl;
}
