package com.example.ChatBot.controller;

import com.example.ChatBot.dto.request.AuthenticationRequest;
import com.example.ChatBot.dto.request.IntrospectRequest;
import com.example.ChatBot.dto.request.LogoutRequest;
import com.example.ChatBot.dto.response.ApiResponse;
import com.example.ChatBot.dto.response.AuthenticationResponse;
import com.example.ChatBot.dto.response.AuthenticationResult;
import com.example.ChatBot.dto.response.IntrospectResponse;
import com.example.ChatBot.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response){

        AuthenticationResult result  = authenticationService.authenticate(request);

        ResponseCookie cookie = ResponseCookie.from("accessToken",  result.getToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Đăng nhập thành công")
                .result(AuthenticationResponse.builder()
                        .role(result.getRole())
                        .build())
                .build();


    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> authenticate(@CookieValue(value = "accessToken", required = false) String token, HttpServletResponse response) throws ParseException, JOSEException {

        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .sameSite("Lax")
                .build();
        authenticationService.logout(token);
        return ApiResponse.<Void>builder()
                .message("Logout successfull!")
                .build();
    }


}
