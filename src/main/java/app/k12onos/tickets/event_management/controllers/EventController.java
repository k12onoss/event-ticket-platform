package app.k12onos.tickets.event_management.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.k12onos.tickets.event.domain.entities.Event;
import app.k12onos.tickets.event_management.domain.requests.CreateEventRequest;
import app.k12onos.tickets.event_management.domain.requests.UpdateEventRequest;
import app.k12onos.tickets.event_management.domain.responses.EventResponse;
import app.k12onos.tickets.event_management.domain.responses.EventSummaryResponse;
import app.k12onos.tickets.event_management.services.EventService;
import app.k12onos.tickets.security.utils.SecurityUtil;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/events")
public class EventController {

    private final EventService eventService;

    EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
        @AuthenticationPrincipal Jwt jwt,
        @Valid @RequestBody CreateEventRequest createEventRequest) {

        UUID userId = SecurityUtil.parseUserId(jwt);

        Event createdEvent = this.eventService.createEvent(userId, createEventRequest);

        EventResponse createEventResponse = EventResponse.from(createdEvent);

        return new ResponseEntity<>(createEventResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EventSummaryResponse>> getEvents(
        @AuthenticationPrincipal Jwt jwt,
        Pageable pageable) {

        UUID userId = SecurityUtil.parseUserId(jwt);

        PagedModel<EventSummaryResponse> events = this.eventService.getEventsByOrganizer(userId, pageable);

        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId) {

        UUID userId = SecurityUtil.parseUserId(jwt);

        Optional<Event> event = this.eventService.getEventByOrganizer(userId, eventId);

        return event.map(EventResponse::from).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID eventId,
        @Valid @RequestBody UpdateEventRequest updateEventRequest) {

        UUID userId = SecurityUtil.parseUserId(jwt);

        Event updatedEvent = this.eventService.updateEventByOrganizer(userId, eventId, updateEventRequest);

        EventResponse updatedEventResponse = EventResponse.from(updatedEvent);

        return ResponseEntity.ok(updatedEventResponse);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId) {
        UUID userId = SecurityUtil.parseUserId(jwt);
        this.eventService.deleteEventByOrganizer(userId, eventId);

        return ResponseEntity.noContent().build();
    }

}
