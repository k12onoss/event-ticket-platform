package app.k12onos.tickets.domain.responses;

import java.util.UUID;

import app.k12onos.tickets.domain.entities.TicketValidation;
import app.k12onos.tickets.domain.enums.TicketValidationStatus;

public record TicketValidationResponse(
        UUID ticketId,
        TicketValidationStatus status) {

    public static TicketValidationResponse from(TicketValidation validation) {
        return new TicketValidationResponse(
                validation.getTicket().getId(),
                validation.getStatus());
    }

}
