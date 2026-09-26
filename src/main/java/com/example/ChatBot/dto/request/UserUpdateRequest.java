package com.example.ChatBot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    @NotBlank(message = "USERNAME_REQUIRED")
    @Size(min = 3, max = 30, message = "USERNAME_INVALID")
    @Pattern(
            regexp = "^[a-zA-Z0-9_]+$",
            message = "USERNAME_INVALID"
    )
    private String username;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 8, max = 100, message = "PASSWORD_INVALID")
    private String password;
}
