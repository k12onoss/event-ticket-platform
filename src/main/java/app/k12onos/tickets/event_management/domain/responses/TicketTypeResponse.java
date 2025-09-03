package app.k12onos.tickets.event_management.domain.responses;

import java.time.LocalDateTime;
import java.util.UUID;

import app.k12onos.tickets.event.domain.entities.TicketType;

public record TicketTypeResponse(
    UUID id,
    String name,
    String description,
    Double price,
    Integer totalAvailable,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {

    public static TicketTypeResponse from(TicketType ticketType) {
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
