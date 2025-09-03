package app.k12onos.tickets.published_event.domain.responses;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import app.k12onos.tickets.event.domain.entities.Event;

public record PublishedEventResponse(
    UUID id,
    String name,
    LocalDateTime start,
    LocalDateTime end,
    String venue,
    LocalDateTime salesStart,
    LocalDateTime salesEnd,
    List<TicketTypeSummaryResponse> ticketTypes) {

    public static PublishedEventResponse from(Event event) {
        List<TicketTypeSummaryResponse> ticketTypes = event
            .getTicketTypes()
            .stream()
            .map(TicketTypeSummaryResponse::from)
            .toList();

        return new PublishedEventResponse(
            event.getId(),
            event.getName(),
            event.getStart(),
            event.getEnd(),
            event.getVenue(),
            event.getSalesStart(),
            event.getSalesEnd(),
            ticketTypes);
    }

}
