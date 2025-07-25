package app.k12onos.tickets.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import app.k12onos.tickets.domain.entities.Event;
import app.k12onos.tickets.domain.entities.TicketType;
import app.k12onos.tickets.domain.entities.User;
import app.k12onos.tickets.domain.requests.CreateEventRequest;
import app.k12onos.tickets.domain.responses.CreateEventResponse;
import app.k12onos.tickets.domain.responses.CreateTicketTypeResponse;

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

    public CreateEventResponse toDto(Event event) {
        List<CreateTicketTypeResponse> ticketTypeResponses = event
                .getTicketTypes()
                .stream()
                .map(ticketTypeMapper::toDto).toList();

        return new CreateEventResponse(
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

}
