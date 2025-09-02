package app.k12onos.tickets.published_event.controllers;

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

import app.k12onos.tickets.event.domain.entities.Event;
import app.k12onos.tickets.published_event.domain.responses.PublishedEventResponse;
import app.k12onos.tickets.published_event.domain.responses.PublishedEventSummaryResponse;
import app.k12onos.tickets.published_event.services.PublishedEventService;

@RestController
@RequestMapping(path = "/api/v1/published-events")
public class PublishedEventController {

    private final PublishedEventService publishedEventService;

    public PublishedEventController(PublishedEventService publishedEventService) {
        this.publishedEventService = publishedEventService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<PublishedEventSummaryResponse> getPublishedEvents(
            @RequestParam(required = false) String q,
            Pageable pageable) {

        Page<PublishedEventSummaryResponse> publishedEvents;

        if (q != null && !q.isBlank()) {
            var events = this.publishedEventService.searchPublishedEvent(q, pageable);
            publishedEvents = events.map(PublishedEventSummaryResponse::from);
        } else {
            publishedEvents = this.publishedEventService.getPublishedEvents(pageable);
        }

        return new PagedModel<>(publishedEvents);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<PublishedEventResponse> getPublishedEvent(@PathVariable UUID eventId) {
        Optional<Event> publishedEvent = this.publishedEventService.getPublishedEvent(eventId);

        return publishedEvent
                .map(PublishedEventResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
