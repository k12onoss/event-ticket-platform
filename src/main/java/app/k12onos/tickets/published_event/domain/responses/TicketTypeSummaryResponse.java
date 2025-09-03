package app.k12onos.tickets.published_event.domain.responses;

import java.util.UUID;

import app.k12onos.tickets.event.domain.entities.TicketType;

public record TicketTypeSummaryResponse(UUID id, String name, String description, Double price) {

    public static TicketTypeSummaryResponse from(TicketType ticketType) {
        return new TicketTypeSummaryResponse(
            ticketType.getId(),
            ticketType.getName(),
            ticketType.getDescription(),
            ticketType.getPrice());
    }

}
