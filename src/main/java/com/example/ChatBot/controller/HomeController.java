package com.example.ChatBot.controller;

import com.example.ChatBot.dto.request.CreateHomeRequest;
import com.example.ChatBot.dto.request.HomeFilterRequest;
import com.example.ChatBot.dto.request.HomeRequest;
import com.example.ChatBot.dto.request.UpdateHomeRequest;
import com.example.ChatBot.dto.response.*;
import com.example.ChatBot.entity.Home;
import com.example.ChatBot.repository.HomeRepository;
import com.example.ChatBot.service.AuthenticationService;
import com.example.ChatBot.service.HomeService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/homes")
public class HomeController {
    @Autowired
    private HomeService homeService;
    AuthenticationService authenticationService;


    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_LANDLORD') and hasAuthority('CREATE_HOME')")
    ApiResponse<CreateHomeResponse> createHome (@RequestBody @Valid CreateHomeRequest request) {
        return ApiResponse.<CreateHomeResponse>builder()
                .result(homeService.CreatHome(request))
                .build();
    }

    //Lấy 1 phòng theo id
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_LANDLORD')")
    public ApiResponse<ViewHomeResponse> viewHome(
            @PathVariable String id,
            Authentication authentication
    ) {
        String idUser = authentication.getName();
        return ApiResponse.<ViewHomeResponse>builder()
                .message("GET_HOME_SUCCESSFULLY")
                .result(homeService.viewHome(idUser, id))
                .build();
    }

    //Lấy danh sách 30 phòng bất kỳ
    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_RENTER') and hasAuthority('GET_HOME')")
    ApiResponse<List<HomeResponse>> getHomes() {
        return ApiResponse.<List<HomeResponse>>builder()
                .result(homeService.getHome())
                .build();
    }

    //Lấy danh sách phòng chủ nhà quản lý
    @GetMapping("/manager")
    @PreAuthorize("hasAuthority('ROLE_LANDLORD') and hasAuthority('GETMANAGER_HOME')")
    public ApiResponse<List<ManagerResponse>> getMyHome(Authentication authentication){
        String id = authentication.getName();
        return ApiResponse.<List<ManagerResponse>>builder()
                .result(homeService.getManager(id))
                .build();
    }

    //Xóa phòng theo chủ nhà
    @DeleteMapping("/{idHome}")
    @PreAuthorize("hasAuthority('ROLE_LANDLORD') and hasAuthority('DELETE_HOME')")
    public ApiResponse<Void> deleteRoom(
            @PathVariable String idHome,
            Authentication authentication
    ) {

        String idUser = authentication.getName();

        homeService.deleteHome(idUser,idHome);

        return ApiResponse.<Void>builder()
                .message("Delete room successfully")
                .build();
    }


    @PutMapping("/{homeId}")
    @PreAuthorize("hasAuthority('ROLE_LANDLORD') and hasAuthority('UPDATE_HOME')")
    public ApiResponse<UpdateHomeResponse> updateHome(
            @PathVariable String homeId,
            @RequestBody UpdateHomeRequest request,
            Authentication authentication
    ) {

        String userId = authentication.getName();

        return ApiResponse.<UpdateHomeResponse>builder()
                .message("HOME_UPDATED_SUCCESSFULLY")
                .result(homeService.updateHome(request, userId,homeId))
                .build();
    }


    @GetMapping("/filterhome")
    @PreAuthorize("hasAuthority('ROLE_RENTER') and hasAuthority('FIND_HOME')")
    public ApiResponse<Page<HomeFilterResponse>> getHome ( @ModelAttribute HomeFilterRequest filter, @PageableDefault(size = 10) Pageable pageable) {
       return ApiResponse.<Page<HomeFilterResponse>>builder()
               .result(homeService.search(filter, pageable))
               .build();
    }
}
