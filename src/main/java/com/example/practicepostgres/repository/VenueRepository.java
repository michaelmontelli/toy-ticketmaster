package com.example.practicepostgres.repository;

import com.example.practicepostgres.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}
