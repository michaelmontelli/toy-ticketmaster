package com.example.practicepostgres.repository;

import com.example.practicepostgres.model.Event;
import com.example.practicepostgres.model.Reservation;
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
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Test
    void findBySeat_IdAndExpiresAtAfter_filtersExpiredReservations() {
        // Given
        var venue = venueRepository.save(Venue.builder()
                .name("Test Venue")
                .city("Test City")
                .capacity(100)
                .build());

        var event = eventRepository.save(Event.builder()
                .name("Test Event")
                .date(LocalDateTime.now().plusDays(1))
                .venue(venue)
                .build());

        var seat = seatRepository.save(Seat.builder()
                .event(event)
                .name("A1")
                .price(new BigDecimal("100.00"))
                .status(SeatStatus.AVAILABLE)
                .build());

        var now = LocalDateTime.now();

        // Expired reservation (1 hour ago)
        reservationRepository.save(Reservation.builder()
                .seat(seat)
                .userEmail("expired1@test.com")
                .reservedAt(now.minusHours(2))
                .expiresAt(now.minusHours(1))
                .build());

        // Active reservation (1 hour from now)
        reservationRepository.save(Reservation.builder()
                .seat(seat)
                .userEmail("active@test.com")
                .reservedAt(now)
                .expiresAt(now.plusHours(1))
                .build());

        // Another expired reservation (30 minutes ago)
        reservationRepository.save(Reservation.builder()
                .seat(seat)
                .userEmail("expired2@test.com")
                .reservedAt(now.minusHours(1))
                .expiresAt(now.minusMinutes(30))
                .build());

        // When
        List<Reservation> activeReservations = reservationRepository
                .findBySeat_IdAndExpiresAtAfter(seat.getId(), now);

        // Then
        assertThat(activeReservations).hasSize(1);
        assertThat(activeReservations.get(0).getUserEmail()).isEqualTo("active@test.com");
        assertThat(activeReservations.get(0).getExpiresAt()).isAfter(now);
    }
}

