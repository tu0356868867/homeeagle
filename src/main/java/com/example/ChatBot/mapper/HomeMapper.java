package com.example.ChatBot.mapper;

import com.example.ChatBot.dto.request.CreateHomeRequest;
import com.example.ChatBot.dto.request.RoleRequest;
import com.example.ChatBot.dto.response.*;
import com.example.ChatBot.entity.Home;
import com.example.ChatBot.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HomeMapper {
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "contact", ignore = true)
    Home toHomeEntity(CreateHomeRequest request);


    CreateHomeResponse toCreateHomeResponse(Home home);
    // Entity → Response
    HomeResponse toHomeResponse(Home home);

    ViewHomeResponse toViewHomeResponse(Home home);

    UpdateHomeResponse toUpdateHomeResponse(Home home);


}
