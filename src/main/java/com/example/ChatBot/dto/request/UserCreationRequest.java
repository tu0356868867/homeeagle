package com.example.ChatBot.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank(message = "USERNAME_REQUIRED")
    @Size(min = 3, max = 30, message = "USERNAME_INVALID")
    @Pattern(
            regexp = "^[a-zA-Z0-9_]+$",
            message = "USERNAME_INVALID"
    )
    String username;

    @NotBlank(message = "FULLNAME_REQUIRED")
    @Size(min = 2, max = 100, message = "FULLNAME_INVALID")
    String fullname;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 8, max = 100, message = "PASSWORD_INVALID")
    String password;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_INVALID")
    String email;

    Set<String> roles;
}
