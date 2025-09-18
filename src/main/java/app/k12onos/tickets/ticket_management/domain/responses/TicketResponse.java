package app.k12onos.tickets.ticket_management.domain.responses;

import java.time.LocalDateTime;
import java.util.UUID;

import app.k12onos.tickets.ticket.domain.dtos.TicketDto;
import app.k12onos.tickets.ticket.domain.enums.TicketStatus;

public record TicketResponse(
    UUID id,
    TicketStatus status,
    String ticketTypeName,
    String description,
    Double price,
    String eventName,
    LocalDateTime eventStart,
    LocalDateTime eventEnd,
    String eventVenue,
    String eventPosterUrl) {

    public static TicketResponse from(TicketDto ticketDto, String posterUrl) {
        return new TicketResponse(
            ticketDto.id(),
            ticketDto.status(),
            ticketDto.ticketTypeName(),
            ticketDto.description(),
            ticketDto.price(),
            ticketDto.eventName(),
            ticketDto.eventStart(),
            ticketDto.eventEnd(),
            ticketDto.eventVenue(),
            posterUrl);
    }
}
