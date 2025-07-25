package app.k12onos.tickets.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.k12onos.tickets.domain.entities.Event;
import app.k12onos.tickets.domain.requests.CreateEventRequest;
import app.k12onos.tickets.domain.responses.CreateEventResponse;
import app.k12onos.tickets.domain.responses.ListEventResponse;
import app.k12onos.tickets.mappers.EventMapper;
import app.k12onos.tickets.services.EventService;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

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

        UUID userId = parseUserId(jwt);

        Event createdEvent = eventService.createEvent(userId, createEventRequest);

        CreateEventResponse createEventResponse = eventMapper.toDto(createdEvent);

        return new ResponseEntity<>(createEventResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedModel<ListEventResponse>> getEvents(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable) {

        UUID userId = parseUserId(jwt);

        PagedModel<ListEventResponse> events = new PagedModel<>(eventService.getEventsByOrganizer(userId, pageable));

        return ResponseEntity.ok(events);
    }

    private UUID parseUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

}
