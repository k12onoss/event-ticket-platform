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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.k12onos.tickets.event_management.domain.requests.ConfirmUploadRequest;
import app.k12onos.tickets.event_management.domain.requests.CreateEventRequest;
import app.k12onos.tickets.event_management.domain.requests.UpdateEventRequest;
import app.k12onos.tickets.event_management.domain.requests.UploadUrlRequest;
import app.k12onos.tickets.event_management.domain.responses.EventResponse;
import app.k12onos.tickets.event_management.domain.responses.EventSummaryResponse;
import app.k12onos.tickets.event_management.domain.responses.UploadUrlResponse;
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
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse createEvent(
        @AuthenticationPrincipal Jwt jwt,
        @Valid @RequestBody CreateEventRequest createRequest) {

        UUID userId = SecurityUtil.parseUserId(jwt);

        return this.eventService.createEvent(userId, createRequest);
    }

    @GetMapping
    public PagedModel<EventSummaryResponse> getEvents(@AuthenticationPrincipal Jwt jwt, Pageable pageable) {
        UUID userId = SecurityUtil.parseUserId(jwt);

        return this.eventService.getEventsByOrganizer(userId, pageable);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId) {
        UUID userId = SecurityUtil.parseUserId(jwt);

        Optional<EventResponse> event = this.eventService.getEventByOrganizer(userId, eventId);

        return event.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{eventId}")
    public EventResponse updateEvent(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID eventId,
        @Valid @RequestBody UpdateEventRequest updateRequest) {

        UUID userId = SecurityUtil.parseUserId(jwt);

        return this.eventService.updateEventByOrganizer(userId, eventId, updateRequest);
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId) {
        UUID userId = SecurityUtil.parseUserId(jwt);
        this.eventService.deleteEventByOrganizer(userId, eventId);
    }

    @PostMapping("/{id}/upload")
    UploadUrlResponse getUploadResponse(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID id,
        @RequestBody UploadUrlRequest uploadUrlRequest) {

        UUID userId = SecurityUtil.parseUserId(jwt);
        return this.eventService.generateUploadUrl(userId, id, uploadUrlRequest);
    }

    @PostMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void confirmUpload(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID id,
        @Valid @RequestBody ConfirmUploadRequest confirmUploadRequest) {

        UUID userId = SecurityUtil.parseUserId(jwt);
        this.eventService.confirmUpload(userId, id, confirmUploadRequest);
    }

}
