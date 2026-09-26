package com.example.ChatBot.controller;

import com.example.ChatBot.dto.request.HomeFilterRequest;
import com.example.ChatBot.dto.request.RoomSearchRequest;
import com.example.ChatBot.dto.request.RoomateRequest;
import com.example.ChatBot.dto.response.*;
import com.example.ChatBot.service.RoomateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roomate")
public class RoomController {
    @Autowired
    private RoomateService roomService;


    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_RENTER') and hasAuthority('CREATE_ROOM')")
    public ApiResponse<RoomateResponse> createRoom(@Valid @RequestBody RoomateRequest request, Authentication authentication) {
        String id = authentication.getName();
        System.out.println(request);
        return ApiResponse.<RoomateResponse>builder()
                .result(roomService.CreateRoomate(request))
                .build(); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_RENTER') and hasAuthority('DELETE_ROOM')")
    ApiResponse<Void> deleteRoom(@PathVariable String id) {
        roomService.deleteRoom(id);
        return ApiResponse.<Void>builder()
                .message("Xóa bài viết thành công")
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_RENTER') and hasAuthority('UPDATE_ROOM')")
    public ApiResponse<RoomateResponse> updatePost(
            @PathVariable("id") String id,
            @RequestBody RoomateRequest request
    ){
        return ApiResponse.<RoomateResponse>builder()
                .result(roomService.updateRoom(id,request))
                .message("Cập nhật roomate thành công")
                .build();
    }


    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_RENTER') and hasAuthority('GET_ROOMATE')")
    ApiResponse<List<ListRoomateResponse>> getRoomates() {
        return ApiResponse.<List<ListRoomateResponse>>builder()
                .result(roomService.getRoomate())
                .build();
    }

    //Tìm roomate theo điều kiện
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_RENTER') and hasAuthority('FIND_ROOMATE')")
    public ApiResponse<Page<RoomSearchResponse>> getHome (@ModelAttribute RoomSearchRequest filter, @PageableDefault(size = 15) Pageable pageable) {
        return ApiResponse.<Page<RoomSearchResponse>>builder()
                .result(roomService.search(filter,pageable))
                .build();
    }

    //Lấy roomate theo id
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAuthority('ROLE_RENTER') and hasAuthority('DETAIL_ROOMATE')")
    public ApiResponse<GetRoomByidResponse> getDetail(@PathVariable String id){
        return ApiResponse.<GetRoomByidResponse>builder()
                .result(roomService.getRoomatebyId(id))
                .build();
    }

}
