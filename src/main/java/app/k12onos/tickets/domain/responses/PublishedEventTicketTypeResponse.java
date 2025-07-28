package app.k12onos.tickets.domain.responses;

import java.util.UUID;

public record PublishedEventTicketTypeResponse(
        UUID id,
        String name,
        String description,
        Double price) {

}
