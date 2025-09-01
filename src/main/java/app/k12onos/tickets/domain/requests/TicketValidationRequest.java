package app.k12onos.tickets.domain.requests;

import app.k12onos.tickets.domain.enums.TicketValidationMethod;

public record TicketValidationRequest(
        TicketValidationMethod validationMethod,
        String token,
        String signature) {

}
