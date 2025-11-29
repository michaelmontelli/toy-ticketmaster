package com.example.practicepostgres.repository;

import com.example.practicepostgres.model.Seat;
import com.example.practicepostgres.model.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByEvent_Id(Long eventId);
    List<Seat> findByEvent_IdAndStatus(Long eventId, SeatStatus status);
}
