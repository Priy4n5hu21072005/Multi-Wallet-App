package com.example.multiwallet.mapper;

import com.example.multiwallet.dto.user.RegisterUserRequest;
import com.example.multiwallet.dto.user.UpdateUserRequest;
import com.example.multiwallet.dto.user.UserResponse;
import com.example.multiwallet.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "createdAt" , ignore = true)
    @Mapping(target = "updatedAt" , ignore = true)
    @Mapping(target = "wallets" , ignore = true)
    User toEntity(RegisterUserRequest request);
    UserResponse toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "createdAt" , ignore = true)
    @Mapping(target = "updatedAt" , ignore = true)
    @Mapping(target = "wallets" , ignore = true)
    void UpdateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);

}
