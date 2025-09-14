package app.k12onos.tickets.published_event.domain.responses;

import java.time.LocalDateTime;
import java.util.UUID;

import app.k12onos.tickets.event.domain.entities.Event;

public record PublishedEventSummaryResponse(
    UUID id,
    String name,
    LocalDateTime start,
    LocalDateTime end,
    String venue,
    String posterUrl) {

    public static PublishedEventSummaryResponse from(Event event, String posterUrl) {
        return new PublishedEventSummaryResponse(
            event.getId(),
            event.getName(),
            event.getStart(),
            event.getEnd(),
            event.getVenue(),
            posterUrl);
    }

}
