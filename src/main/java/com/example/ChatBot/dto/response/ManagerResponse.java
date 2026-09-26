package com.example.ChatBot.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManagerResponse {
    private String idHome;
    private String address;
    private Integer  price;
    private Integer quantity;
    private String type;
}
