package vn.base.edumate.vision;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageAnalyzeBatch {
    private List<ImageAnalyze> images;
}

