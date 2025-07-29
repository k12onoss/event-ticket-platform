package app.k12onos.tickets.domain.responses;

import java.util.List;
import java.util.UUID;

import app.k12onos.tickets.domain.enums.TicketStatus;

public record ListTicketResponse(
        UUID id,
        TicketStatus status,
        List<TicketTypeSummaryResponse> ticketType) {

}
