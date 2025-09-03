package app.k12onos.tickets.published_event.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import app.k12onos.tickets.event.domain.entities.Event;
import app.k12onos.tickets.event.repositories.EventRepository;
import app.k12onos.tickets.published_event.domain.responses.PublishedEventSummaryResponse;

@Service
public class PublishedEventService {

    private final EventRepository eventRepository;

    public PublishedEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Page<PublishedEventSummaryResponse> getPublishedEvents(Pageable pageable) {
        Page<Event> publishedEvents = this.eventRepository.findPublishedEvents(pageable);
        return publishedEvents.map(PublishedEventSummaryResponse::from);
    }

    public Page<Event> searchPublishedEvent(String query, Pageable pageable) {
        return this.eventRepository.searchPublishedEvents(query, pageable);
    }

    public Optional<Event> getPublishedEvent(UUID id) {
        return this.eventRepository.findPublishedEvent(id);
    }

}
