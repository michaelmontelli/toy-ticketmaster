package com.example.practicepostgres.service;

import com.example.practicepostgres.model.Event;
import com.example.practicepostgres.model.Seat;
import com.example.practicepostgres.model.SeatStatus;
import com.example.practicepostgres.model.Venue;
import com.example.practicepostgres.repository.EventRepository;
import com.example.practicepostgres.repository.ReservationRepository;
import com.example.practicepostgres.repository.SeatRepository;
import com.example.practicepostgres.repository.VenueRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void createReservation_shouldSucceedForAvailableSeat() {
        // Given
        var venue = venueRepository.save(Venue.builder()
                .name("Test Venue")
                .city("NYC")
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

        // When
        var reservation = reservationService.createReservation(seat.getId(), "user@test.com");

        // Then
        assertThat(reservation).isNotNull();
        assertThat(reservation.getId()).isNotNull();
        assertThat(reservation.getUserEmail()).isEqualTo("user@test.com");
        assertThat(reservation.getSeat().getId()).isEqualTo(seat.getId());
        assertThat(reservation.getExpiresAt()).isAfter(LocalDateTime.now());

        var updatedSeat = seatRepository.findById(seat.getId()).orElseThrow();
        assertThat(updatedSeat.getStatus()).isEqualTo(SeatStatus.RESERVED);
    }

    @Test
    void createReservation_shouldFailWhenSeatNotAvailable() {
        // Given
        var venue = venueRepository.save(Venue.builder()
                .name("Test Venue")
                .city("NYC")
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
                .status(SeatStatus.SOLD)
                .build());

        // When/Then
        assertThatThrownBy(() -> reservationService.createReservation(seat.getId(), "user@test.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Seat is not available");
    }

    @Test
    void createReservation_shouldPreventDoubleBookingWithPessimisticLocking() throws InterruptedException {
        // Given
        var venue = venueRepository.save(Venue.builder()
                .name("Test Venue")
                .city("NYC")
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

        // When: Two concurrent reservation attempts
        var threadCount = 2;
        var latch = new CountDownLatch(threadCount);
        var successCount = new AtomicInteger(0);
        var failureCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            var userEmail = "user" + i + "@test.com";
            executor.submit(() -> {
                try {
                    reservationService.createReservation(seat.getId(), userEmail);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // Then: Only one reservation should succeed
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failureCount.get()).isEqualTo(1);

        var reservations = reservationRepository.findBySeat_IdAndExpiresAtAfter(
                seat.getId(), LocalDateTime.now());
        assertThat(reservations).hasSize(1);

        var updatedSeat = seatRepository.findById(seat.getId()).orElseThrow();
        assertThat(updatedSeat.getStatus()).isEqualTo(SeatStatus.RESERVED);
    }
}
