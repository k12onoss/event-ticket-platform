package app.k12onos.tickets.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.k12onos.tickets.domain.entities.Event;
import app.k12onos.tickets.domain.entities.User;
import app.k12onos.tickets.domain.requests.CreateEventRequest;
import app.k12onos.tickets.domain.requests.UpdateEventRequest;
import app.k12onos.tickets.domain.responses.ListEventResponse;
import app.k12onos.tickets.exceptions.EventNotFoundException;
import app.k12onos.tickets.exceptions.UserNotFoundException;
import app.k12onos.tickets.mappers.EventMapper;
import app.k12onos.tickets.repositories.EventRepository;
import app.k12onos.tickets.repositories.UserRepository;

@Service
public class EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    EventService(
            UserRepository userRepository,
            EventRepository eventRepository,
            EventMapper eventMapper) {

        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Transactional
    public Event createEvent(UUID organizerId, CreateEventRequest event) {
        User organizer = userRepository
                .findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + organizerId + " not found."));

        Event eventToCreate = eventMapper.toEntity(organizer, event);

        return eventRepository.save(eventToCreate);
    }

    public Page<ListEventResponse> getEventsByOrganizer(UUID organizerId, Pageable pageable) {
        return eventRepository.findEventsByOrganizer(organizerId, pageable);
    }

    public Optional<Event> getEventByOrganizer(UUID organizerId, UUID eventId) {
        return eventRepository.findEventByOrganizer(organizerId, eventId);
    }

    @Transactional
    public Event updateEventByOrganizer(
            UUID organizerId,
            UUID eventId,
            UpdateEventRequest updateRequest) {

        Event existingEvent = eventRepository
                .findEventByOrganizer(organizerId, eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " not found"));

        eventMapper.updateEntity(existingEvent, updateRequest);

        return eventRepository.save(existingEvent);
    }

    @Transactional
    public void deleteEventByOrganizer(UUID organizerId, UUID eventId) {
        getEventByOrganizer(organizerId, eventId).ifPresent(eventRepository::delete);
    }

}
