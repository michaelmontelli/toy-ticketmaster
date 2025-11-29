package com.example.practicepostgres.api;

import com.example.practicepostgres.model.Seat;
import com.example.practicepostgres.model.SeatStatus;

import java.math.BigDecimal;

public record SeatResponse(
    Long id,
    Long eventId,
    String name,
    BigDecimal price,
    SeatStatus status
) {
    public static SeatResponse from(Seat seat) {
        return new SeatResponse(
            seat.getId(),
            seat.getEventId(),
            seat.getName(),
            seat.getPrice(),
            seat.getStatus()
        );
    }
}
