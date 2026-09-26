package com.example.ChatBot.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewHomeResponse {
    private String address;
    private String  contact;
    private Integer  price;
    private Integer quantity;
    private String type;
    private Integer area;
    private String picture;
    private String describe;
}
