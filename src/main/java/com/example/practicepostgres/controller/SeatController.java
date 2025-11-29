package com.example.practicepostgres.controller;

import com.example.practicepostgres.api.SeatResponse;
import com.example.practicepostgres.model.Seat;
import com.example.practicepostgres.model.SeatStatus;
import com.example.practicepostgres.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/{eventId}/seats")
    public ResponseEntity<List<SeatResponse>> listSeats(
        @PathVariable Long eventId,
        @RequestParam(required = false) SeatStatus status
    ) {
        List<Seat> seats = seatService.listSeats(eventId, status);
        List<SeatResponse> responses = seats.stream()
                .map(SeatResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
