package com.example.practicepostgres.repository;

import com.example.practicepostgres.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    public List<Reservation> findBySeat_IdAndExpiresAtAfter(Long seatId, LocalDateTime time);
}
