package com.example.practicepostgres.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

    @GetMapping
    public ResponseEntity<List<Event>> getEvents() {

    }
}
