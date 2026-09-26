package com.example.ChatBot.mapper;

import com.example.ChatBot.dto.request.RoleRequest;
import com.example.ChatBot.dto.response.RoleResponse;
import com.example.ChatBot.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = PermissionMapper.class
)
public interface RoleMapper {

    // Request → Entity
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    // Entity → Response
    RoleResponse toRoleResponse(Role role);
}
