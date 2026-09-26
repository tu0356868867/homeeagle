package com.example.ChatBot.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomateRequest {

    @NotBlank(message = "ADDRESS_REQUIRED")
    @Size(min = 3, max = 100, message = "ADDRESS_INVALID")
    private String address;

    @NotBlank(message = "PRICE_REQUIRED")
    @Size(min = 3, max = 15, message = "PRICE_INVALID")
    private String price;

    @NotBlank(message = "TYPE_REQUIRED")
    private String type;

    @NotBlank(message = "PICTURE_REQUIRED")
    @Size(min = 3, max = 2048, message = "PICTURE_INVALID")
    private String picture;

    @NotNull(message = "ROOMATE_REQUIRED")
    @Min(value = 1, message = "ROOMATE_INVALID")
    private Integer max_roomate;

    @NotNull(message = "AREA_REQUIRED")
    @Min(value = 1, message = "AREA_INVALID")
    private Integer area;

    @Size(min = 0, max = 200, message = "TOO_LONG")
    private String infor;

    @NotBlank(message = "CONTACT_REQUIRED")
    @Size(min = 3, max = 15, message = "CONTACT_INVALID")
    @Pattern(
            regexp = "^[0-9]+$",
            message = "CONTACT_INVALID"
    )
    private String contact;
}
