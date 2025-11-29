package com.example.practicepostgres.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationRequest {
    @NotNull
    private Long seatId;

    @NotBlank
    private String userEmail;
}
