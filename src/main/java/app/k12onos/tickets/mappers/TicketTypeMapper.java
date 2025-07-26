package app.k12onos.tickets.mappers;

import org.springframework.stereotype.Component;

import app.k12onos.tickets.domain.entities.Event;
import app.k12onos.tickets.domain.entities.TicketType;
import app.k12onos.tickets.domain.requests.CreateTicketTypeRequest;
import app.k12onos.tickets.domain.responses.TicketTypeResponse;

@Component
public class TicketTypeMapper {

    TicketType toEntity(CreateTicketTypeRequest createTicketTypeRequest, Event event) {
        TicketType ticketType = new TicketType();
        ticketType.setName(createTicketTypeRequest.name());
        ticketType.setDescription(createTicketTypeRequest.description());
        ticketType.setPrice(createTicketTypeRequest.price());
        ticketType.setTotalAvailable(createTicketTypeRequest.totalAvailable());
        ticketType.setEvent(event);

        return ticketType;
    }

    TicketTypeResponse toDto(TicketType ticketType) {
        return new TicketTypeResponse(
                ticketType.getId(),
                ticketType.getName(),
                ticketType.getDescription(),
                ticketType.getPrice(),
                ticketType.getTotalAvailable(),
                ticketType.getCreatedAt(),
                ticketType.getUpdatedAt());
    }

}
