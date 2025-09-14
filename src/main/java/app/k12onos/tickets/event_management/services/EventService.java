package app.k12onos.tickets.event_management.services;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.k12onos.tickets.event.domain.entities.Event;
import app.k12onos.tickets.event.repositories.EventRepository;
import app.k12onos.tickets.event.services.S3Service;
import app.k12onos.tickets.event_management.domain.requests.ConfirmUploadRequest;
import app.k12onos.tickets.event_management.domain.requests.CreateEventRequest;
import app.k12onos.tickets.event_management.domain.requests.UpdateEventRequest;
import app.k12onos.tickets.event_management.domain.requests.UploadUrlRequest;
import app.k12onos.tickets.event_management.domain.responses.EventResponse;
import app.k12onos.tickets.event_management.domain.responses.EventSummaryResponse;
import app.k12onos.tickets.event_management.domain.responses.UploadUrlResponse;
import app.k12onos.tickets.event_management.exceptions.EventNotFoundException;
import app.k12onos.tickets.security.domain.entities.User;
import app.k12onos.tickets.security.exceptions.UserNotFoundException;
import app.k12onos.tickets.security.repositories.UserRepository;

@Service
public class EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final S3Service s3Service;

    EventService(UserRepository userRepository, EventRepository eventRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.s3Service = s3Service;
    }

    @Transactional
    public EventResponse createEvent(UUID organizerId, CreateEventRequest event) {
        User organizer = this.userRepository
            .findById(organizerId)
            .orElseThrow(() -> new UserNotFoundException("User with id " + organizerId + " not found."));

        Event createdEvent = this.eventRepository.save(event.toEntity(organizer));

        String posterUrl = this.s3Service.generateReadUrl(createdEvent.getPosterKey());
        String bannerUrl = this.s3Service.generateReadUrl(createdEvent.getBannerKey());
        return EventResponse.from(createdEvent, posterUrl, bannerUrl);
    }

    public PagedModel<EventSummaryResponse> getEventsByOrganizer(UUID organizerId, Pageable pageable) {
        Page<Event> events = this.eventRepository.findEventsByOrganizer(organizerId, pageable);
        Page<EventSummaryResponse> eventSummaries = events
            .map(e -> EventSummaryResponse.from(e, this.s3Service.generateReadUrl(e.getPosterKey())));
        return new PagedModel<>(eventSummaries);
    }

    public Optional<EventResponse> getEventByOrganizer(UUID organizerId, UUID eventId) {
        Optional<Event> event = this.eventRepository.findEventByOrganizer(organizerId, eventId);
        return event.map(e -> {
            String posterUrl = this.s3Service.generateReadUrl(e.getPosterKey());
            String bannerUrl = this.s3Service.generateReadUrl(e.getBannerKey());
            return EventResponse.from(e, posterUrl, bannerUrl);
        });
    }

    @Transactional
    public EventResponse updateEventByOrganizer(UUID organizerId, UUID eventId, UpdateEventRequest updateRequest) {
        Event existingEvent = this.eventRepository
            .findEventByOrganizer(organizerId, eventId)
            .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " not found"));

        updateRequest.updateEntity(existingEvent);
        Event updatedEvent = this.eventRepository.save(existingEvent);

        String posterUrl = this.s3Service.generateReadUrl(updatedEvent.getPosterKey());
        String bannerUrl = this.s3Service.generateReadUrl(updatedEvent.getBannerKey());
        return EventResponse.from(updatedEvent, posterUrl, bannerUrl);
    }

    @Transactional
    public void deleteEventByOrganizer(UUID organizerId, UUID eventId) {
        this.eventRepository.findEventByOrganizer(organizerId, eventId).ifPresent(event -> {
            if (event.getPosterKey() != null) {
                this.s3Service.deleteFile(event.getPosterKey());
            }
            if (event.getBannerKey() != null) {
                this.s3Service.deleteFile(event.getBannerKey());
            }
            this.eventRepository.delete(event);
        });
    }

    public UploadUrlResponse generateUploadUrl(UUID organizerId, UUID eventId, UploadUrlRequest uploadUrlRequest) {
        Event _ = this.eventRepository
            .findEventByOrganizer(organizerId, eventId)
            .orElseThrow(() -> new EventNotFoundException("Event does not exists"));

        String key = "events/"
            + eventId
            + "/"
            + uploadUrlRequest.imageType()
            + "/"
            + UUID.randomUUID()
            + "."
            + uploadUrlRequest.contentType().substring(6);
        String url = this.s3Service
            .generatePresignedPutUrl(key, uploadUrlRequest.contentType(), Duration.ofMinutes(10));

        return new UploadUrlResponse(url, key);
    }

    public void confirmUpload(UUID organizerId, UUID eventId, ConfirmUploadRequest confirmUploadRequest) {
        Event event = this.eventRepository
            .findEventByOrganizer(organizerId, eventId)
            .orElseThrow(() -> new EventNotFoundException("Event does not exists"));

        if (confirmUploadRequest.type().equals("poster")) {
            event.setPosterKey(confirmUploadRequest.key());
        } else if (confirmUploadRequest.type().equals("banner")) {
            event.setBannerKey(confirmUploadRequest.key());
        }

        Event _ = this.eventRepository.save(event);
    }

}
