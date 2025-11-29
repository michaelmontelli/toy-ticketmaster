package com.example.practicepostgres.controller;

import com.example.practicepostgres.api.CreateReservationRequest;
import com.example.practicepostgres.model.Event;
import com.example.practicepostgres.model.Reservation;
import com.example.practicepostgres.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> listReservations() {
        List<Reservation> reservations = reservationService.listReservations();
        return ResponseEntity.ok(reservations);

    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @Valid @RequestBody CreateReservationRequest request
    ) {
        var reservation = reservationService.createReservation(
            request.getSeatId(),
            request.getUserEmail()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }
}
