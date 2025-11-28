package com.example.practicepostgres;

import com.example.practicepostgres.model.Event;
import com.example.practicepostgres.model.Venue;
import com.example.practicepostgres.repository.EventRepository;
import com.example.practicepostgres.repository.VenueRepository;
import jakarta.annotation.Nullable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {
    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;

    public DataLoader(VenueRepository venueRepository, EventRepository eventRepository) {
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
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
    }
}
