package com.example.ChatBot.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateFeedbackRequest {
    @Size(min = 0, max = 300, message = "TOO_LONG")
    String feedback;
}
