package app.k12onos.tickets.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.k12onos.tickets.domain.entities.Event;
import app.k12onos.tickets.domain.responses.ListPublishedEventResponse;
import app.k12onos.tickets.domain.responses.PublishedEventResponse;
import app.k12onos.tickets.mappers.EventMapper;
import app.k12onos.tickets.services.EventService;

@RestController
@RequestMapping(path = "/api/v1/published-events")
public class PublishedEventController {

    private final EventMapper eventMapper;
    private final EventService eventService;

    public PublishedEventController(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<ListPublishedEventResponse> getPublishedEvents(
            @RequestParam(required = false) String q,
            Pageable pageable) {

        Page<ListPublishedEventResponse> publishedEvents;

        if (q != null && !q.isBlank()) {
            var events = eventService.searchPublishedEvent(q, pageable);
            publishedEvents = events.map(eventMapper::toListPublishedEventResponse);
        } else {
            publishedEvents = eventService.getPublishedEvents(pageable);
        }

        return new PagedModel<>(publishedEvents);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<PublishedEventResponse> getPublishedEvent(@PathVariable UUID eventId) {
        Optional<Event> publishedEvent = eventService.getPublishedEvent(eventId);

        return publishedEvent
                .map(eventMapper::toPublishedEventResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
