package app.k12onos.tickets.domain.responses;

import java.time.LocalDateTime;
import java.util.UUID;

import app.k12onos.tickets.domain.entities.Ticket;
import app.k12onos.tickets.domain.enums.TicketStatus;

public record TicketResponse(
                UUID id,
                TicketStatus status,
                String ticketTypeName,
                String description,
                Double price,
                String eventName,
                LocalDateTime eventStart,
                LocalDateTime eventEnd,
                String eventVenue) {

        public static TicketResponse from(Ticket ticket) {
                return new TicketResponse(
                                ticket.getId(),
                                ticket.getStatus(),
                                ticket.getTicketType().getName(),
                                ticket.getTicketType().getDescription(),
                                ticket.getTicketType().getPrice(),
                                ticket.getTicketType().getEvent().getName(),
                                ticket.getTicketType().getEvent().getStart(),
                                ticket.getTicketType().getEvent().getEnd(),
                                ticket.getTicketType().getEvent().getVenue());
        }

}
