package com.example.multiwallet.mapper;

import com.example.multiwallet.dto.user.RegisterUserRequest;
import com.example.multiwallet.dto.user.UserResponse;
import com.example.multiwallet.entity.User;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(RegisterUserRequest request);
    UserResponse toResponse(User user);

}
