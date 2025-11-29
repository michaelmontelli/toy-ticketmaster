package com.example.practicepostgres.repository;

import com.example.practicepostgres.model.Event;
import com.example.practicepostgres.model.Seat;
import com.example.practicepostgres.model.SeatStatus;
import com.example.practicepostgres.model.Venue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SeatRepositoryTest {
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Test
    void findByEventId_shouldReturnSeatsForEvent() {
        // Given
        Venue venue = venueRepository.save(Venue.builder()
                .name("Test Venue")
                .city("NYC")
                .capacity(100)
                .build());

        Event event = eventRepository.save(Event.builder()
                .name("Test Event")
                .date(LocalDateTime.now())
                .venue(venue)
                .build());

        seatRepository.save(Seat.builder()
                .event(event)
                .name("A1")
                .price(new BigDecimal("50.00"))
                .status(SeatStatus.AVAILABLE)
                .build());

        // When
        List<Seat> seats = seatRepository.findByEvent_Id(event.getId());

        // Then
        assertThat(seats).hasSize(1);
        assertThat(seats.get(0).getName()).isEqualTo("A1");
    }

    @Test
    void findByEvent_IdAndStatus_shouldFilterByStatus() {
        // Given
        Venue venue = venueRepository.save(Venue.builder()
                .name("Test Venue")
                .city("NYC")
                .capacity(100)
                .build());

        Event event = eventRepository.save(Event.builder()
                .name("Test Event")
                .date(LocalDateTime.now())
                .venue(venue)
                .build());

        seatRepository.save(Seat.builder()
                .event(event)
                .name("A1")
                .price(new BigDecimal("50"))
                .status(SeatStatus.AVAILABLE)
                .build());

        seatRepository.save(Seat.builder()
                .event(event)
                .name("A2")
                .price(new BigDecimal("50"))
                .status(SeatStatus.SOLD)
                .build());

        // When
        List<Seat> available = seatRepository.findByEvent_IdAndStatus(
                event.getId(), SeatStatus.AVAILABLE);

        // Then
        assertThat(available).hasSize(1);
        assertThat(available.get(0).getName()).isEqualTo("A1");
    }
}
