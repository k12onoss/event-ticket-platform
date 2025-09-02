package app.k12onos.tickets.event_management.domain.responses;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import app.k12onos.tickets.event.domain.entities.Event;
import app.k12onos.tickets.event.domain.enums.EventStatus;

public record EventResponse(
        UUID id,
        String name,
        LocalDateTime start,
        LocalDateTime end,
        String venue,
        LocalDateTime salesStart,
        LocalDateTime salesEnd,
        List<TicketTypeResponse> ticketTypes,
        EventStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static EventResponse from(Event event) {
        List<TicketTypeResponse> ticketTypeResponses = event
                .getTicketTypes()
                .stream()
                .map(TicketTypeResponse::from)
                .toList();

        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getStart(),
                event.getEnd(),
                event.getVenue(),
                event.getSalesStart(),
                event.getSalesEnd(),
                ticketTypeResponses,
                event.getStatus(),
                event.getCreatedAt(),
                event.getUpdatedAt());
    }

}
