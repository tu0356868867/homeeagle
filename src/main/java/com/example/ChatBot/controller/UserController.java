package com.example.ChatBot.controller;


import com.example.ChatBot.dto.request.CreateFeedbackRequest;
import com.example.ChatBot.dto.request.UserCreationRequest;
import com.example.ChatBot.dto.request.UserUpdateRequest;
import com.example.ChatBot.dto.response.ApiResponse;
import com.example.ChatBot.dto.response.CreateFeedbackResponse;
import com.example.ChatBot.dto.response.UserResponse;
import com.example.ChatBot.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('ROLE_RENTER') OR hasAuthority('ROLE_LANDLORD')")
    ApiResponse<Void> updateUser(@CookieValue(value = "accessToken", required = false) String token,@RequestBody @Valid UserUpdateRequest request){
        userService.updateUser(request);
        return ApiResponse.<Void>builder()
                .message("Cập nhật thông tin thành công!")
                .build();
    }

    @PostMapping("/feedback")
    @PreAuthorize("hasAuthority('ROLE_RENTER') OR hasAuthority('ROLE_LANDLORD') OR hasAuthority('SEND_FEEDBACK')")
    ApiResponse<Void> createFeedback(@RequestBody @Valid CreateFeedbackRequest request){
        CreateFeedbackResponse response = userService.getFeedbackUser(request);
        return ApiResponse.<Void>builder()
                .message(response.getMessage())
                .build();
    }

}
