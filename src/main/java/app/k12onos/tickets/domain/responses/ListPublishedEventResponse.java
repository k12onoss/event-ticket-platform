package app.k12onos.tickets.domain.responses;

import java.time.LocalDateTime;
import java.util.UUID;

import app.k12onos.tickets.domain.entities.Event;

public record ListPublishedEventResponse(
        UUID id,
        String name,
        LocalDateTime start,
        LocalDateTime end,
        String venue) {

    public static ListPublishedEventResponse from(Event event) {
        return new ListPublishedEventResponse(
                event.getId(),
                event.getName(),
                event.getStart(),
                event.getEnd(),
                event.getVenue());
    }

}
