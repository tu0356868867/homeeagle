package com.example.ChatBot.service;

import com.example.ChatBot.dto.request.HomeFilterRequest;
import com.example.ChatBot.dto.request.UpdateHomeRequest;
import com.example.ChatBot.dto.response.*;
import com.example.ChatBot.dto.request.CreateHomeRequest;
import com.example.ChatBot.entity.Home;
import com.example.ChatBot.entity.Manager;
import com.example.ChatBot.entity.User;
import com.example.ChatBot.exception.AppException;
import com.example.ChatBot.exception.ErrorCode;
import com.example.ChatBot.mapper.HomeMapper;
import com.example.ChatBot.repository.HomeRepository;
import com.example.ChatBot.repository.ManagerRepository;
import com.example.ChatBot.repository.UserRepository;
import com.example.ChatBot.specification.HomeSpecification;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class HomeService {
    HomeRepository homeRepository;
    UserRepository userRepository;
    HomeMapper homeMapper;
    ManagerRepository managerRepository;

    public List<HomeResponse> getHome() {
        return homeRepository.findBystatusDeleteFalse(PageRequest.of(0, 30))
                .map(homeMapper::toHomeResponse)
                .getContent();
    }

    public ViewHomeResponse viewHome(String idUser, String id){
        Manager manager = managerRepository.findByHome_Id(id)
                .orElseThrow(() ->
                        new AppException(ErrorCode.HOME_NOT_EXISTED)
                );

        // Kiểm tra quyền sở hữu
        if (!manager.getUser().getId().equals(idUser)) {
            throw new AppException(ErrorCode.HOME_NOT_VEW);
        }

        Home viewHome = manager.getHome();

        return homeMapper.toViewHomeResponse(viewHome);

    }


    public List<ManagerResponse> getManager(String userId) {
            return managerRepository.findHomesByUserId(userId);
    }



    @Transactional
    public void deleteHome(String idUser, String idHome) {

        Manager manager = managerRepository.findByHome_Id(idHome)
                .orElseThrow(() ->
                        new AppException(ErrorCode.HOME_NOT_EXISTED)
                );

        Home home = homeRepository.findById(idHome)
                .orElseThrow(() ->
                        new AppException(ErrorCode.HOME_NOT_EXISTED)
                );

        // Kiểm tra phòng có thuộc user hiện tại không
        if (!manager.getUser().getId().equals(idUser)) {
            throw new AppException(ErrorCode.HOME_NOT_OWNER);
        }

        home.setStatusDelete(true);
        homeRepository.save(home);
    }



    public CreateHomeResponse CreatHome(CreateHomeRequest request){
        boolean exists =
                homeRepository.existsByAddressAndType(
                        request.getAddress(),
                        request.getType()
                );
        if (exists) {
            throw new AppException(
                    ErrorCode.POST_EXISTED
            );
        }

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String userId = authentication.getName();
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_EXIST)
                );

        Manager manager = new Manager();
        manager.setUser(user);
        Home home = homeMapper.toHomeEntity(request);
        home.setPrice(parsePrice(request.getPrice()));
        home.setContact(normalizeVietnamPhone(request.getContact()));
        homeRepository.save(home);
        manager.setHome(home);
        managerRepository.save(manager);
        return homeMapper.toCreateHomeResponse(homeRepository.save(home));
    }

    //Chỉnh sửa phòng theo id
    public UpdateHomeResponse updateHome(UpdateHomeRequest request, String idUser, String idHome){
        Manager manager = managerRepository.findByHome_Id(idHome)
                .orElseThrow(() ->
                        new AppException(ErrorCode.HOME_NOT_EXISTED)
                );

        // Kiểm tra phòng có thuộc user hiện tại không
        if (!manager.getUser().getId().equals(idUser)) {
            throw new AppException(ErrorCode.HOME_NOT_OWNER);
        }

        Home home = manager.getHome();

        home.setAddress(request.getAddress());
        home.setContact(normalizeVietnamPhone(request.getContact()));
        home.setQuantity(request.getQuantity());
        home.setPrice(parsePrice(request.getPrice()));
        home.setType(request.getType());
        home.setArea(request.getArea());
        home.setPicture(request.getPicture());
        home.setDescribe(request.getDescribe());

        return homeMapper.toUpdateHomeResponse(homeRepository.save(home));
    }



    public Page<HomeFilterResponse> search(HomeFilterRequest filter, Pageable pageable){
        Specification<Home> spec = HomeSpecification.filter(filter);

        Page<HomeFilterResponse> response = homeRepository.findAll(spec, pageable)
                .map(item -> HomeFilterResponse.builder()
                        .id(item.getId())
                        .address(item.getAddress())
                        .price(item.getPrice())
                        .type(item.getType())
                        .picture(item.getPicture())
                        .describe(item.getDescribe())
                        .build());

        return response;

    }

    public String normalizeVietnamPhone(String phone) {

        if (phone == null || phone.isBlank()) {
            return null;
        }

        // Xóa khoảng trắng, ., -, (, )
        String value = phone.replaceAll("[\\s.()\\-]", "");

        // +84912345678 -> 0912345678
        if (value.startsWith("+84")) {
            value = "0" + value.substring(3);
        }

        // 84912345678 -> 0912345678
        else if (value.startsWith("84") && value.length() == 11) {
            value = "0" + value.substring(2);
        }

        return value;
    }

    public Integer parsePrice(String input) {

        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Giá không được để trống");
        }

        String value = input
                .trim()
                .toLowerCase()
                .replace("vnd", "")
                .replace("vnđ", "")
                .replace("đ", "")
                .trim();

        // 5tr, 5tr5, 5.5tr
        if (value.endsWith("tr")) {

            String number = value.substring(0, value.length() - 2)
                    .trim()
                    .replace(",", ".");

            try {
                BigDecimal million = new BigDecimal(number);

                return million
                        .multiply(BigDecimal.valueOf(1_000_000))
                        .intValueExact();
            } catch (Exception e) {
                throw new IllegalArgumentException("Giá không hợp lệ");
            }
        }

        // 5m, 5.5m
        if (value.endsWith("m")) {

            String number = value.substring(0, value.length() - 1)
                    .trim()
                    .replace(",", ".");

            try {
                BigDecimal million = new BigDecimal(number);

                return million
                        .multiply(BigDecimal.valueOf(1_000_000))
                        .intValueExact();

            } catch (Exception e) {
                throw new IllegalArgumentException("Giá không hợp lệ");
            }
        }

        // Giá dạng số:
        // 5000000
        // 5.000.000
        // 5,000,000
        value = value
                .replace(".", "")
                .replace(",", "")
                .replace(" ", "");

        if (!value.matches("\\d+")) {
            throw new IllegalArgumentException("Giá không hợp lệ");
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Giá quá lớn");
        }
    }
}
