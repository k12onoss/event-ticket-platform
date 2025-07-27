package app.k12onos.tickets.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.k12onos.tickets.domain.responses.PublishedEventResponse;
import app.k12onos.tickets.services.EventService;

@RestController
@RequestMapping(path = "/api/v1/published-events")
public class PublishedEventController {

    private final EventService eventService;

    public PublishedEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<PagedModel<PublishedEventResponse>> getPublishedEvents(Pageable pageable) {
        Page<PublishedEventResponse> publishedEvents = eventService.getPublishedEvents(pageable);

        return ResponseEntity.ok(new PagedModel<>(publishedEvents));
    }

}
