package vn.base.edumate.tag;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.base.edumate.common.base.DataResponse;
import vn.base.edumate.common.constant.SystemConstant;
import vn.base.edumate.common.util.TagType;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tag")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagController {
    TagService tagService;

    @GetMapping
    DataResponse<List<TagResponse>> getAll() {
        return DataResponse.<List<TagResponse>>builder()
                .message(SystemConstant.SUCCESS)
                .data(tagService.getAllTags())
                .build();
    }

    @GetMapping("/type/{type}")
    DataResponse<List<TagResponse>> getAllByTagType(@PathVariable("type") TagType tagType) {
        List<TagResponse> dataResponses = tagService.getTagByType(tagType);
        return DataResponse.<List<TagResponse>>builder()
                .message(SystemConstant.SUCCESS)
                .data(dataResponses)
                .build();
    }

    @PostMapping
    DataResponse<TagResponse> createTag(@RequestBody CreateTagRequest createTagRequest) {
        return DataResponse.<TagResponse>builder()
                .message(SystemConstant.SUCCESS)
                .data(tagService.createTag(createTagRequest))
                .build();
    }
}
