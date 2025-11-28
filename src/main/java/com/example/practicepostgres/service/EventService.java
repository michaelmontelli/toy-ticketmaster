package com.example.practicepostgres.service;

import com.example.practicepostgres.model.Event;
import com.example.practicepostgres.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> listEvents() {
        return eventRepository.findAll();
    }

    public List<Event> createEvents(List<Event> events) {
        return eventRepository.saveAll(events);
    }
}
