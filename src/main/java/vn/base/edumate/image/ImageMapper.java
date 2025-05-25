package vn.base.edumate.image;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageResponse toImageResponse(Image image);
}
