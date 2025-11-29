package com.example.practicepostgres.service;

import com.example.practicepostgres.model.Seat;
import com.example.practicepostgres.model.SeatStatus;
import com.example.practicepostgres.repository.SeatRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    // Lombok NonNull annotation does runtime check for null and throws error if null
    public List<Seat> listSeats(@NonNull Long eventId, SeatStatus status) {
        if (status == null) {
            return seatRepository.findByEvent_Id(eventId);
        }
        return seatRepository.findByEvent_IdAndStatus(eventId, status);
    }

}
