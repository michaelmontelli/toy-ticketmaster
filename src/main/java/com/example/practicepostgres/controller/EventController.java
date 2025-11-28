package com.example.practicepostgres.controller;

import com.example.practicepostgres.api.CreateEventRequest;
import com.example.practicepostgres.model.Event;
import com.example.practicepostgres.model.Venue;
import com.example.practicepostgres.repository.VenueRepository;
import com.example.practicepostgres.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final VenueRepository venueRepository;

    public EventController(EventService eventService, VenueRepository venueRepository) {
        this.eventService = eventService;
        this.venueRepository = venueRepository;
    }

    @GetMapping
    public ResponseEntity<List<Event>> listEvents() {
        List<Event> events = eventService.listEvents();
        return ResponseEntity.ok().body(events);
    }

    @PostMapping
    public ResponseEntity<List<Event>> createEvents(
            @Valid @RequestBody List<CreateEventRequest> requests
    ) {
        List<Event> results = eventService.createEvents(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }
}
