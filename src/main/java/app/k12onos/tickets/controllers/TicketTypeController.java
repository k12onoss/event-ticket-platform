package app.k12onos.tickets.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.k12onos.tickets.services.TicketService;

@RestController
@RequestMapping(path = "/api/v1/events/{eventId}/ticket-types")
public class TicketTypeController {

    private final TicketService ticketService;

    public TicketTypeController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{ticketTypeId}/tickets")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void purchaseTicket(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID ticketTypeId) {

        UUID userId = parseUserId(jwt);

        ticketService.purchaseTicket(userId, ticketTypeId);
    }

    private UUID parseUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

}