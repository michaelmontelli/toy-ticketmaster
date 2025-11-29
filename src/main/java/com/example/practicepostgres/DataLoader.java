package com.example.practicepostgres;

import com.example.practicepostgres.model.Event;
import com.example.practicepostgres.model.Seat;
import com.example.practicepostgres.model.SeatStatus;
import com.example.practicepostgres.model.Venue;
import com.example.practicepostgres.repository.EventRepository;
import com.example.practicepostgres.repository.SeatRepository;
import com.example.practicepostgres.repository.VenueRepository;
import jakarta.annotation.Nullable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    public DataLoader(
        VenueRepository venueRepository,
        EventRepository eventRepository,
        SeatRepository seatRepository
    ) {
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public void run(@Nullable String... args) throws Exception {
        Venue madisonSquareGarden = Venue.builder()
                .name("Madison Square Garden")
                .city("New York")
                .capacity(20000)
                .build();
        venueRepository.save(madisonSquareGarden);

        Venue staples = Venue.builder()
                .name("Staples Center")
                .city("Los Angeles")
                .capacity(19000)
                .build();
        venueRepository.save(staples);

        Event concert = Event.builder()
                .name("Taylor Swift Concert")
                .date(LocalDateTime.of(2024, 6, 15, 19, 0))
                .venue(madisonSquareGarden)
                .build();
        eventRepository.save(concert);

        Event game = Event.builder()
                .name("NBA Finals")
                .date(LocalDateTime.of(2024, 6, 20, 20, 30))
                .venue(staples)
                .build();
        eventRepository.save(game);

        createSeats(concert, game);
    }

    private void createSeats(Event concert, Event game) {
        // Add seats for Taylor Swift Concert
        List<Seat> concertSeats = new ArrayList<>();

        // Premium seats
        for (int i = 1; i <= 10; i++) {
            concertSeats.add(Seat.builder()
                    .event(concert)
                    .name("A" + i)
                    .price(new BigDecimal("250.00"))
                    .status(SeatStatus.AVAILABLE)
                    .build());
        }

        // Mid-tier seats
        for (int i = 1; i <= 20; i++) {
            concertSeats.add(Seat.builder()
                    .event(concert)
                    .name("B" + i)
                    .price(new BigDecimal("150.00"))
                    .status(SeatStatus.AVAILABLE)
                    .build());
        }

        // Budget seats
        for (int i = 1; i <= 15; i++) {
            concertSeats.add(Seat.builder()
                    .event(concert)
                    .name("C" + i)
                    .price(new BigDecimal("75.00"))
                    .status(SeatStatus.AVAILABLE)
                    .build());
        }

        // Mark a few as reserved/sold
        concertSeats.get(0).setStatus(SeatStatus.RESERVED);
        concertSeats.get(1).setStatus(SeatStatus.SOLD);
        concertSeats.get(10).setStatus(SeatStatus.RESERVED);

        seatRepository.saveAll(concertSeats);

        // Add seats for NBA Finals
        List<Seat> gameSeats = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            gameSeats.add(Seat.builder()
                    .event(game)
                    .name("VIP" + i)
                    .price(new BigDecimal("500.00"))
                    .status(SeatStatus.AVAILABLE)
                    .build());
        }

        gameSeats.get(0).setStatus(SeatStatus.SOLD);

        seatRepository.saveAll(gameSeats);
    }
}
