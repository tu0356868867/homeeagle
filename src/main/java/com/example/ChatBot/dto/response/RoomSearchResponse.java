package com.example.ChatBot.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomSearchResponse {
    private String id;
    private String address;
    private Integer  price;
    private String type;
    private String picture;
    private String infor;

}
