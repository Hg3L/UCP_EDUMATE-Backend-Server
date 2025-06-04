package vn.base.edumate.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import vn.base.edumate.user.dto.UserResponse;
import vn.base.edumate.user.dto.request.UpdateUserRequest;
import vn.base.edumate.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UpdateUserRequest updateUserRequest);
}
