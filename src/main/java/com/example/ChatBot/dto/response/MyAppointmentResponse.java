package com.example.ChatBot.dto.response;

import com.example.ChatBot.status.AppointmentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MyAppointmentResponse {
    private String idAppointment;
    private String address;
    private LocalDate createdAt;
    private AppointmentStatus status;
}
