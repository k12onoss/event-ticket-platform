package app.k12onos.tickets.ticket.domain.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import app.k12onos.tickets.ticket.domain.enums.TicketStatus;

public record TicketDto(
    UUID id,
    TicketStatus status,
    String ticketTypeName,
    String description,
    Double price,
    String eventName,
    LocalDateTime eventStart,
    LocalDateTime eventEnd,
    String eventVenue,
    String eventPosterKey) {}
