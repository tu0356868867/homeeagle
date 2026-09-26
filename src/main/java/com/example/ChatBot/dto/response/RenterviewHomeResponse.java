package com.example.ChatBot.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RenterviewHomeResponse {
    String address;
    String contact;
    String type;
    LocalDate createdAt;
    String picture;
}
