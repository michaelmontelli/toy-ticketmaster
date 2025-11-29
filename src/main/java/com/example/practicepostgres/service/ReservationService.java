package com.example.practicepostgres.service;

import com.example.practicepostgres.model.Reservation;
import com.example.practicepostgres.model.SeatStatus;
import com.example.practicepostgres.repository.ReservationRepository;
import com.example.practicepostgres.repository.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservationService {
    private static final Duration RESERVATION_EXPIRATION = Duration.ofMinutes(5);

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;

    public ReservationService(
        ReservationRepository reservationRepository,
        SeatRepository seatRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.seatRepository = seatRepository;
    }

    public List<Reservation> listReservations() {
        return reservationRepository.findAll();
    }

    public Reservation createReservation(
        @NonNull Long seatId,
        @NonNull String userEmail
    ) {
        LocalDateTime currentTime = LocalDateTime.now();

        var seat = seatRepository.findById(seatId).orElseThrow(
            () -> new RuntimeException("Seat not found")
        );
        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            throw new RuntimeException("Seat is not available");
        }

        List<Reservation> existingReservations = getExistingReservationsForSeat(seatId,  currentTime);
        if (existingReservations.size() > 1) {
            throw new IllegalStateException("Data integrity issue: multiple active reservations found");
        }

        if (!existingReservations.isEmpty()) {
            throw new RuntimeException("Seat already has an active reservation");
        }

        var reservation = reservationRepository.save(
            Reservation.builder()
                    .seat(seat)
                    .userEmail(userEmail)
                    .reservedAt(currentTime)
                    .expiresAt(currentTime.plus(RESERVATION_EXPIRATION))
                    .build()
        );
        seat.setStatus(SeatStatus.RESERVED);
        seatRepository.save(seat);

        return reservation;
    }

    private List<Reservation> getExistingReservationsForSeat(Long seatId, LocalDateTime time) {
        return reservationRepository.findBySeat_IdAndExpiresAtAfter(seatId, time);
    }
}
