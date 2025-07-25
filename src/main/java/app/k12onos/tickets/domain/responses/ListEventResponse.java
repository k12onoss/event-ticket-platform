package app.k12onos.tickets.domain.responses;

import java.time.LocalDateTime;
import java.util.UUID;

import app.k12onos.tickets.domain.enums.EventStatus;

public record ListEventResponse(
        UUID id,
        String name,
        LocalDateTime start,
        LocalDateTime end,
        String venue,
        LocalDateTime salesStart,
        LocalDateTime salesEnd,
        EventStatus status) {

}
