package app.k12onos.tickets.domain.responses;

import java.util.UUID;

import app.k12onos.tickets.domain.entities.Ticket;
import app.k12onos.tickets.domain.enums.TicketStatus;

public record ListTicketResponse(
                UUID id,
                TicketStatus status,
                TicketTypeSummaryResponse ticketType) {

        public static ListTicketResponse from(Ticket ticket) {
                return new ListTicketResponse(
                                ticket.getId(),
                                ticket.getStatus(),
                                TicketTypeSummaryResponse.from(ticket.getTicketType()));
        }

}
