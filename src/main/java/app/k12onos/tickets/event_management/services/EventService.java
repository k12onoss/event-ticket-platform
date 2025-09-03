package app.k12onos.tickets.event_management.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.k12onos.tickets.event.domain.entities.Event;
import app.k12onos.tickets.event.repositories.EventRepository;
import app.k12onos.tickets.event_management.domain.requests.CreateEventRequest;
import app.k12onos.tickets.event_management.domain.requests.UpdateEventRequest;
import app.k12onos.tickets.event_management.domain.responses.EventSummaryResponse;
import app.k12onos.tickets.event_management.exceptions.EventNotFoundException;
import app.k12onos.tickets.security.domain.entities.User;
import app.k12onos.tickets.security.exceptions.UserNotFoundException;
import app.k12onos.tickets.security.repositories.UserRepository;

@Service
public class EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    EventService(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Event createEvent(UUID organizerId, CreateEventRequest event) {
        User organizer = this.userRepository
            .findById(organizerId)
            .orElseThrow(() -> new UserNotFoundException("User with id " + organizerId + " not found."));

        Event eventToCreate = event.toEntity(organizer);

        return this.eventRepository.save(eventToCreate);
    }

    public Page<EventSummaryResponse> getEventsByOrganizer(UUID organizerId, Pageable pageable) {
        Page<Event> events = this.eventRepository.findEventsByOrganizer(organizerId, pageable);
        return events.map(EventSummaryResponse::from);
    }

    public Optional<Event> getEventByOrganizer(UUID organizerId, UUID eventId) {
        return this.eventRepository.findEventByOrganizer(organizerId, eventId);
    }

    @Transactional
    public Event updateEventByOrganizer(UUID organizerId, UUID eventId, UpdateEventRequest updateRequest) {

        Event existingEvent = this.eventRepository
            .findEventByOrganizer(organizerId, eventId)
            .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " not found"));

        updateRequest.updateEntity(existingEvent);

        return this.eventRepository.save(existingEvent);
    }

    @Transactional
    public void deleteEventByOrganizer(UUID organizerId, UUID eventId) {
        this.getEventByOrganizer(organizerId, eventId).ifPresent(this.eventRepository::delete);
    }

}
