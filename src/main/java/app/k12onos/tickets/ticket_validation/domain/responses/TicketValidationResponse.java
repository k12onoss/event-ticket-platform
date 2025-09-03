package app.k12onos.tickets.ticket_validation.domain.responses;

import java.util.UUID;

import app.k12onos.tickets.ticket_validation.domain.entities.TicketValidation;
import app.k12onos.tickets.ticket_validation.domain.enums.TicketValidationStatus;

public record TicketValidationResponse(UUID ticketId, TicketValidationStatus status) {

    public static TicketValidationResponse from(TicketValidation validation) {
        return new TicketValidationResponse(validation.getTicket().getId(), validation.getStatus());
    }

}
