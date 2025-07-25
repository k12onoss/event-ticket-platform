package app.k12onos.tickets.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.k12onos.tickets.domain.entities.Event;
import app.k12onos.tickets.domain.requests.CreateEventRequest;
import app.k12onos.tickets.domain.responses.CreateEventResponse;
import app.k12onos.tickets.mappers.EventMapper;
import app.k12onos.tickets.services.EventService;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/api/v1/events")
public class EventController {

    private final EventMapper eventMapper;
    private final EventService eventService;

    EventController(EventMapper eventMapper, EventService eventService) {
        this.eventMapper = eventMapper;
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<CreateEventResponse> createEvent(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateEventRequest createEventRequest) {

        UUID userId = UUID.fromString(jwt.getSubject());

        Event createdEvent = eventService.createEvent(userId, createEventRequest);

        CreateEventResponse createEventResponse = eventMapper.toDto(createdEvent);

        return new ResponseEntity<>(createEventResponse, HttpStatus.CREATED);
    }

}
