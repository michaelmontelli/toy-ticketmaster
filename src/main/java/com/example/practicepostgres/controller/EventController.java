package com.example.practicepostgres.controller;

import com.example.practicepostgres.model.Event;
import com.example.practicepostgres.service.EventService;
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

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<Event>> listEvents() {
        List<Event> events;
        try {
            events = eventService.listEvents();
            return ResponseEntity.ok().body(events);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<List<Event>> createEvents(@RequestBody List<Event> events) {
        List<Event> results;
        try {
            results = eventService.createEvents(events);
            return ResponseEntity.status(HttpStatus.CREATED).body(results);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
