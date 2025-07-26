package app.k12onos.tickets.mappers;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.k12onos.tickets.domain.entities.Event;
import app.k12onos.tickets.domain.entities.TicketType;
import app.k12onos.tickets.domain.entities.User;
import app.k12onos.tickets.domain.requests.CreateEventRequest;
import app.k12onos.tickets.domain.requests.UpdateEventRequest;
import app.k12onos.tickets.domain.requests.UpdateTicketTypeRequest;
import app.k12onos.tickets.domain.responses.EventResponse;
import app.k12onos.tickets.domain.responses.TicketTypeResponse;
import app.k12onos.tickets.exceptions.TicketTypeNotFoundException;

@Component
public class EventMapper {

    private final TicketTypeMapper ticketTypeMapper;

    EventMapper(TicketTypeMapper ticketTypeMapper) {
        this.ticketTypeMapper = ticketTypeMapper;
    }

    public Event toEntity(User organizer, CreateEventRequest createRequest) {
        Event event = new Event();

        List<TicketType> ticketTypes = createRequest
                .ticketTypes()
                .stream()
                .map((createTicketType) -> ticketTypeMapper.toEntity(createTicketType, event))
                .toList();

        event.setName(createRequest.name());
        event.setStart(createRequest.start());
        event.setEnd(createRequest.end());
        event.setVenue(createRequest.venue());
        event.setSalesStart(createRequest.salesStart());
        event.setSalesEnd(createRequest.salesEnd());
        event.setStatus(createRequest.status());
        event.setOrganizer(organizer);
        event.setTicketTypes(ticketTypes);

        return event;
    }

    public EventResponse toDto(Event event) {
        List<TicketTypeResponse> ticketTypeResponses = event
                .getTicketTypes()
                .stream()
                .map(ticketTypeMapper::toDto).toList();

        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getStart(),
                event.getEnd(),
                event.getVenue(),
                event.getSalesStart(),
                event.getSalesEnd(),
                ticketTypeResponses,
                event.getCreatedAt(),
                event.getUpdatedAt());
    }

    public void updateEntity(Event event, UpdateEventRequest updateRequest) {
        event.setName(updateRequest.name());
        event.setStart(updateRequest.start());
        event.setEnd(updateRequest.end());
        event.setVenue(updateRequest.venue());
        event.setSalesStart(updateRequest.salesStart());
        event.setSalesEnd(updateRequest.salesEnd());
        event.setStatus(updateRequest.status());

        Set<UUID> ticketTypeIds = updateRequest.ticketTypes()
                .stream()
                .map(UpdateTicketTypeRequest::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        event.getTicketTypes().removeIf((ticketType) -> !ticketTypeIds.contains(ticketType.getId()));

        Map<UUID, TicketType> existingTicketTypesMap = event
                .getTicketTypes()
                .stream()
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        for (UpdateTicketTypeRequest newTicketType : updateRequest.ticketTypes()) {
            if (newTicketType.id() == null) {
                TicketType ticketType = ticketTypeMapper.toEntity(newTicketType, event);
                event.getTicketTypes().add(ticketType);
            } else if (existingTicketTypesMap.containsKey(newTicketType.id())) {
                TicketType existingTicketType = existingTicketTypesMap.get(newTicketType.id());
                ticketTypeMapper.updateEntity(existingTicketType, newTicketType);
            } else {
                throw new TicketTypeNotFoundException("TicketType with id " + newTicketType.id() + " not found");
            }
        }
    }

}
