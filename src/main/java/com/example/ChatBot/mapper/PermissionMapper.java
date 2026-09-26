package com.example.ChatBot.mapper;

import com.example.ChatBot.dto.request.PermissionRequest;
import com.example.ChatBot.dto.response.PermissionResponse;
import com.example.ChatBot.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
