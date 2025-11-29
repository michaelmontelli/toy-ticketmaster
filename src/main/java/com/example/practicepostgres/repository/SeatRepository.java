package com.example.practicepostgres.repository;

import com.example.practicepostgres.model.Seat;
import com.example.practicepostgres.model.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByEvent_Id(Long eventId);
    List<Seat> findByEvent_IdAndStatus(Long eventId, SeatStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT s
        FROM Seat s
        WHERE s.id = :id
    """)
    Optional<Seat> findByIdForUpdate(@Param("id") Long id);
}
