package com.example.practicepostgres.service;

import com.example.practicepostgres.api.CreateEventRequest;
import com.example.practicepostgres.repository.EventRepository;
import com.example.practicepostgres.repository.VenueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void createEvents_withInvalidVenueId_shouldThrowException() {
        // Given
        CreateEventRequest request = new CreateEventRequest(
                "Concert", LocalDateTime.now(), 999L);
        when(venueRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> eventService.createEvents(List.of(request)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Venue not found");
    }
}
