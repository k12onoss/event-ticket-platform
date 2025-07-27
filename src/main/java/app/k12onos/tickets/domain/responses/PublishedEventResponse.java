package app.k12onos.tickets.domain.responses;

import java.time.LocalDateTime;
import java.util.UUID;

public record PublishedEventResponse(
        UUID id,
        String name,
        LocalDateTime start,
        LocalDateTime end,
        String venue) {

}
