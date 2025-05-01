package vn.base.edumate.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.base.edumate.common.util.TagType;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagServiceImpl implements TagService {
    TagRepository tagRepository;
    TagMapper tagMapper;

    @Override
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream().map(tagMapper::toResponse).toList();
    }

    @Override
    public List<TagResponse> getTagByType(TagType type) {
        AtomicReference<List<TagResponse>> tagResponses = new AtomicReference<>(new ArrayList<>());
        tagRepository
                .findByTagType(type)
                .ifPresentOrElse(
                        tags -> tagResponses.set(
                                    tags.stream().map(tagMapper::toResponse).toList())
                        ,
                        () -> {
                            throw new ResourceNotFoundException(ErrorCode.TAG_NOT_EXISTED);
                        });
        return tagResponses.get();
    }

    @Override
    public TagResponse getTagById(Long id) {
        return null;
    }

    @Override
    public TagResponse createTag(CreateTagRequest createTagRequest) {
        return tagMapper.toResponse(tagRepository.save(tagMapper.toModel(createTagRequest)));
    }
}
