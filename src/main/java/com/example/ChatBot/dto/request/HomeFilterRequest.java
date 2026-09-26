package com.example.ChatBot.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomeFilterRequest {
    private String address;
    private Integer  minprice;
    private Integer  maxprice;
    private String type;
}
