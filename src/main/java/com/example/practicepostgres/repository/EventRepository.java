package com.example.practicepostgres.repository;

import com.example.practicepostgres.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
