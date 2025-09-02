package app.k12onos.tickets.ticket_validation.domain.requests;

import app.k12onos.tickets.ticket_validation.domain.enums.TicketValidationMethod;

public record TicketValidationRequest(
        TicketValidationMethod validationMethod,
        String token,
        String signature) {

}
