package com.example.ChatBot.mapper;

import com.example.ChatBot.dto.request.RoomateRequest;
import com.example.ChatBot.dto.response.ListRoomateResponse;
import com.example.ChatBot.dto.response.RoomateResponse;
import com.example.ChatBot.entity.Roomate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomateResponse toRoomateResponse(Roomate roomate);

    ListRoomateResponse toLRResponse (Roomate roomate);


    void updatePost(
            @MappingTarget Roomate roomate,
            RoomateRequest request
    );
}
