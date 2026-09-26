package com.example.ChatBot.service;

import com.example.ChatBot.dto.request.CreateFeedbackRequest;
import com.example.ChatBot.dto.request.UserCreationRequest;
import com.example.ChatBot.dto.request.UserUpdateRequest;
import com.example.ChatBot.dto.response.CreateFeedbackResponse;
import com.example.ChatBot.dto.response.UserResponse;
import com.example.ChatBot.entity.InvalidatedToken;
import com.example.ChatBot.entity.Role;
import com.example.ChatBot.entity.User;
import com.example.ChatBot.exception.AppException;
import com.example.ChatBot.exception.ErrorCode;
import com.example.ChatBot.exception.UserAlreadyExistsException;
import com.example.ChatBot.mapper.UserMapper;
import com.example.ChatBot.repository.InvalidatedTokenRepository;
import com.example.ChatBot.repository.RoleRepository;
import com.example.ChatBot.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepository invalidatedTokenRepository;
    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request){

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = request.getRoles()
                .stream()
                .map(roleName -> roleRepository.findById(roleName)
                        .orElseThrow(() ->
                                new AppException(ErrorCode.ROLE_NOT_EXIST)))
                .collect(Collectors.toSet());


        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void updateUser( UserUpdateRequest request){
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String userId = authentication.getName();
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.INVALID_ACCESS)
                );


        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        user.setPassword(request.getPassword());
        user.setUsername(request.getUsername());
        userRepository.save(user);
    }

    public CreateFeedbackResponse getFeedbackUser(CreateFeedbackRequest request){
        CreateFeedbackResponse response = new CreateFeedbackResponse();
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String userId = authentication.getName();
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.INVALID_ACCESS)
                );
        if(user.getFeedback() != null){
            response.setMessage("Vui lòng liên hệ nhân viên hỗ trợ để được khắc phục sự cố!");
        }else{
            user.setFeedback(request.getFeedback());
            userRepository.save(user);
            response.setMessage("Cảm ơn bạn đã gửi feedback!");
        }
        return response;
    }



}
