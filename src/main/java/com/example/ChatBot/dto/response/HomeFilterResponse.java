package com.example.ChatBot.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomeFilterResponse {
    private String id;
    private String address;
    private Integer  price;
    private String type;
    private String picture;
    private String describe;
}
