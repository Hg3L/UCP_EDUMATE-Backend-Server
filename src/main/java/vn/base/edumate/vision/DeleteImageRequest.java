package vn.base.edumate.vision;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DeleteImageRequest {

    private List<Long> ids;
}
