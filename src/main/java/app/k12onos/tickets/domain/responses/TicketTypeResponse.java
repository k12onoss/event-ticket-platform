package app.k12onos.tickets.domain.responses;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketTypeResponse(
        UUID id,
        String name,
        String description,
        Double price,
        Integer totalAvailable,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

}
