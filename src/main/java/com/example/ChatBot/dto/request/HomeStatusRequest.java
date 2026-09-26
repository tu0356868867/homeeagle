package com.example.ChatBot.dto.request;

import com.example.ChatBot.status.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomeStatusRequest {
    @NotNull
    AppointmentStatus status;
}
