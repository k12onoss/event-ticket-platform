package app.k12onos.tickets.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.k12onos.tickets.domain.entities.TicketValidation;
import app.k12onos.tickets.domain.requests.TicketValidationRequest;
import app.k12onos.tickets.domain.responses.TicketValidationResponse;
import app.k12onos.tickets.services.TicketValidationService;

@RestController
@RequestMapping(path = "/api/v1/ticket-validations")
public class TicketValidationController {

    private final TicketValidationService ticketValidationService;

    public TicketValidationController(TicketValidationService ticketValidationService) {
        this.ticketValidationService = ticketValidationService;
    }

    @PostMapping
    TicketValidationResponse validateTicket(@RequestBody TicketValidationRequest validationRequest) {

        TicketValidation validation = ticketValidationService.validateTicket(validationRequest);

        return TicketValidationResponse.from(validation);
    }

}
