package app.k12onos.tickets.event_management.domain.responses;

import java.time.LocalDateTime;
import java.util.UUID;

import app.k12onos.tickets.event.domain.entities.Event;
import app.k12onos.tickets.event.domain.enums.EventStatus;

public record EventSummaryResponse(
    UUID id,
    String name,
    LocalDateTime start,
    LocalDateTime end,
    String venue,
    LocalDateTime salesStart,
    LocalDateTime salesEnd,
    EventStatus status,
    String posterUrl) {

    public static EventSummaryResponse from(Event event, String posterUrl) {
        return new EventSummaryResponse(
            event.getId(),
            event.getName(),
            event.getStart(),
            event.getEnd(),
            event.getVenue(),
            event.getSalesStart(),
            event.getSalesEnd(),
            event.getStatus(),
            posterUrl);
    }
}
