package com.example.ChatBot.service;

import com.example.ChatBot.dto.request.RoomSearchRequest;
import com.example.ChatBot.dto.request.RoomateRequest;
import com.example.ChatBot.dto.response.*;
import com.example.ChatBot.entity.Roomate;
import com.example.ChatBot.entity.User;
import com.example.ChatBot.mapper.RoomMapper;
import com.example.ChatBot.repository.RoomateRepository;
import com.example.ChatBot.repository.UserRepository;
import com.example.ChatBot.specification.RoomSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomateService {
    RoomateRepository roomateRepository;
    UserRepository userRepository;
    RoomMapper roomMapper;


    //Lấy phòng theo id
    public GetRoomByidResponse getRoomatebyId(String id){
        Roomate roomate = roomateRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Post không tồn tại"));

        return GetRoomByidResponse.builder()
                .address(roomate.getAddress())
                .contact(roomate.getContact())
                .max_roomate(roomate.getMax_roomate())
                .type(roomate.getType())
                .build();


    }
    //Lọc phòng theo điều kiện
    public Page<RoomSearchResponse> search(RoomSearchRequest filter, Pageable pageable) {
        Specification<Roomate> spec = RoomSpecification.filter(filter);

        Page<RoomSearchResponse> response = roomateRepository.findAll(spec, pageable)
                .map(item -> RoomSearchResponse.builder()
                        .id(item.getId())
                        .address(item.getAddress())
                        .price(item.getPrice())
                        .type(item.getType())
                        .picture(item.getPicture())
                        .infor(item.getInfor())
                        .build());

        return response;
    }

    //Lấy danh sách 30 phòng bất kỳ
    public List<ListRoomateResponse> getRoomate() {
        return roomateRepository.findAll(PageRequest.of(0, 30))
                .map(roomMapper::toLRResponse)
                .getContent();
    }

    @PreAuthorize("hasRole('RENTER')")
    public RoomateResponse CreateRoomate(RoomateRequest request){

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String userId = authentication.getName();

        if(roomateRepository.existsByUserId(userId)){
            throw new RuntimeException(
                    "Mỗi user chỉ được tạo 1 bài post"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Roomate roomate = Roomate.builder()
                .max_roomate(request.getMax_roomate())
                .infor(request.getInfor())
                .price(parsePrice(request.getPrice()))
                .type(request.getType())
                .picture(request.getPicture())
                .user(user)
                .address(request.getAddress())
                .contact(normalizeVietnamPhone(request.getContact()))
                .area(request.getArea())
                .build();

        roomate = roomateRepository.save(roomate);
        return roomMapper.toRoomateResponse(roomate);
    }

    @PreAuthorize("hasRole('RENTER')")
    public void deleteRoom(String id){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String roomate_ID = authentication.getName();

        Roomate roomate = roomateRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Post không tồn tại"));

        if(!roomate.getUser().getId().equals(roomate_ID)){
            throw new RuntimeException("Bạn không có quyền xóa bài viết này");
        }

        roomateRepository.delete(roomate);
    }

    @PreAuthorize("hasRole('RENTER')")
    public RoomateResponse updateRoom(String id, RoomateRequest request){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Roomate roomate = roomateRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Post không tồn tại"));

        String roomate_ID = authentication.getName();

        if(!roomate.getUser().getId().equals(roomate_ID)){
            throw new RuntimeException("Bạn không có quyền chỉnh sửa bài viết này");
        }

        roomMapper.updatePost(roomate, request);

        return roomMapper.toRoomateResponse(roomateRepository.save(roomate));
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
