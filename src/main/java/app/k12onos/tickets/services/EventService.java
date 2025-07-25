package app.k12onos.tickets.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import app.k12onos.tickets.domain.entities.Event;
import app.k12onos.tickets.domain.entities.User;
import app.k12onos.tickets.domain.requests.CreateEventRequest;
import app.k12onos.tickets.exceptions.UserNotFoundException;
import app.k12onos.tickets.mappers.EventMapper;
import app.k12onos.tickets.repositories.EventRepository;
import app.k12onos.tickets.repositories.UserRepository;

@Service
public class EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    EventService(UserRepository userRepository, EventRepository eventRepository, EventMapper eventMapper) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    public Event createEvent(UUID organizerId, CreateEventRequest event) {
        User organizer = userRepository
                .findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + organizerId + " not found."));

        Event eventToCreate = eventMapper.toEntity(organizer, event);

        return eventRepository.save(eventToCreate);
    }

}
