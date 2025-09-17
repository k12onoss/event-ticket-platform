package app.k12onos.tickets.published_event.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import app.k12onos.tickets.event.domain.entities.Event;
import app.k12onos.tickets.event.domain.enums.EventStatus;
import app.k12onos.tickets.event.repositories.EventRepository;
import app.k12onos.tickets.event.services.S3Service;
import app.k12onos.tickets.published_event.domain.responses.PublishedEventResponse;
import app.k12onos.tickets.published_event.domain.responses.PublishedEventSummaryResponse;

@Service
public class PublishedEventService {

    private final EventRepository eventRepository;
    private final S3Service s3Service;

    public PublishedEventService(EventRepository eventRepository, S3Service s3Service) {
        this.eventRepository = eventRepository;
        this.s3Service = s3Service;
    }

    public PagedModel<PublishedEventSummaryResponse> getPublishedEvents(Pageable pageable) {
        Page<Event> publishedEvents = this.eventRepository.findByStatus(EventStatus.PUBLISHED, pageable);
        Page<PublishedEventSummaryResponse> publishedEventSummaries = publishedEvents
            .map(e -> PublishedEventSummaryResponse.from(e, this.s3Service.generateReadUrl(e.getPosterKey())));
        return new PagedModel<>(publishedEventSummaries);
    }

    public PagedModel<PublishedEventSummaryResponse> searchPublishedEvent(String query, Pageable pageable) {
        Page<Event> publishedEvents = this.eventRepository.searchPublishedEvents(query, pageable);
        Page<PublishedEventSummaryResponse> publishedEventSummaries = publishedEvents
            .map(e -> PublishedEventSummaryResponse.from(e, this.s3Service.generateReadUrl(e.getPosterKey())));
        return new PagedModel<>(publishedEventSummaries);
    }

    public Optional<PublishedEventResponse> getPublishedEvent(UUID id) {
        Optional<Event> publishedEvent = this.eventRepository.findEventByStatus(id, EventStatus.PUBLISHED);

        return publishedEvent
            .map(e -> PublishedEventResponse.from(e, this.s3Service.generateReadUrl(e.getBannerKey())));
    }

}
