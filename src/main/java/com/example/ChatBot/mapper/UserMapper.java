package com.example.ChatBot.mapper;

import com.example.ChatBot.dto.request.UserCreationRequest;
import com.example.ChatBot.dto.response.UserResponse;
import com.example.ChatBot.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);
}
