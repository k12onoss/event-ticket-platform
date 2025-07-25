package app.k12onos.tickets.domain.responses;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateEventResponse(
                UUID id,
                String name,
                LocalDateTime start,
                LocalDateTime end,
                String venue,
                LocalDateTime salesStart,
                LocalDateTime salesEnd,
                List<CreateTicketTypeResponse> ticketTypes,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {

}
