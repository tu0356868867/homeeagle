package com.example.ChatBot.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListRoomateResponse {
    String id;
    String address;
    String picture;
    Integer price;
    String type;
    String infor;
}
