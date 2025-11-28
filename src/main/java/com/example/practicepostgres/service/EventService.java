package com.example.practicepostgres.service;

import com.example.practicepostgres.api.CreateEventRequest;
import com.example.practicepostgres.model.Event;
import com.example.practicepostgres.repository.EventRepository;
import com.example.practicepostgres.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    public EventService(EventRepository eventRepository,  VenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    public List<Event> listEvents() {
        return eventRepository.findAll();
    }

    public List<Event> createEvents(List<CreateEventRequest> requests) {
        List<Event> events = requests.stream()
                .map(request -> {
                    var venue = venueRepository.findById(request.getVenueId())
                            .orElseThrow(() -> new RuntimeException("Venue not found"));
                    return Event.builder()
                            .name(request.getName())
                            .date(request.getDate())
                            .venue(venue)
                            .build();
                })
                .toList();
        return eventRepository.saveAll(events);
    }
}
