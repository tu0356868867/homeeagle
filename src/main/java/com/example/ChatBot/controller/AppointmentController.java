package com.example.ChatBot.controller;

import com.example.ChatBot.dto.request.AppointmentRequest;
import com.example.ChatBot.dto.request.HomeStatusRequest;
import com.example.ChatBot.dto.response.*;
import com.example.ChatBot.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    @PostMapping("/{idHome}")
    @PreAuthorize("hasAuthority('ROLE_RENTER')")
    public ApiResponse<AppointmentResponse> createAppointment(
            @PathVariable String idHome,
            @Valid @RequestBody AppointmentRequest request,
            Authentication authentication
    ) {
        String userId = authentication.getName();


        return ApiResponse.<AppointmentResponse>builder()
                .message("Đặt lịch thành công")
                .result(appointmentService.createAppointment(request,idHome,userId))
                .build();


    }

    //Lấy danh sách lịch xem phòng theo renter
    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_RENTER')")
    public ApiResponse<List<MyAppointmentResponse>> getMyAppointment(Authentication authentication){
        String id = authentication.getName();
        return ApiResponse.<List<MyAppointmentResponse>>builder()
                .result(appointmentService.getAppointment(id))
                .build();
    }



    //Lấy danh sách lịch nghỉ theo Landlord
    @GetMapping("/landlord")
    @PreAuthorize("hasAuthority('ROLE_LANDLORD') and hasAuthority('APPOINTMENT_HOME')")
    public ApiResponse<List<LandlordAppointmentResponse>> getLaAppointment(Authentication authentication){
        String id = authentication.getName();
        return ApiResponse.<List<LandlordAppointmentResponse>>builder()
                .result(appointmentService.getLAResponse(id))
                .build();
    }

    @DeleteMapping("/{idAppointment}")
    @PreAuthorize("hasAuthority('ROLE_RENTER')")
    public ApiResponse<Void> deleteAppointment(
        @PathVariable String idAppointment,
        Authentication authentication
    ) {

    String idUser = authentication.getName();
        appointmentService.deleteAppointment( idUser, idAppointment);

    return ApiResponse.<Void>builder()
            .message("Delete successfully")
            .build();
}

    //Xem lịch hẹn trong popup
    @GetMapping("/renter/{id}")
    @PreAuthorize("hasAuthority('ROLE_RENTER')")
    public ApiResponse<RenterviewHomeResponse> viewAppointmentbyRenter(
            @PathVariable String id,
            Authentication authentication
    ) {
        String idUser = authentication.getName();
        return ApiResponse.<RenterviewHomeResponse>builder()
                .result( appointmentService.getAppointmentbyId(idUser,id))
                .build();
    }

    //Cập nhật trạng thái phòng
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_RENTER')")
    public ApiResponse<Void> statusHome(
            @PathVariable String id,
            Authentication authentication,
            @RequestBody HomeStatusRequest request
    ){
        String idUser = authentication.getName();
        appointmentService.updateStatus(idUser, id ,request);

        return ApiResponse.<Void>builder()
                .message("Update successful!")
                .build();

    }


}
