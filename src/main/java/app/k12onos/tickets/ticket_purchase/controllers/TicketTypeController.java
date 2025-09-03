package app.k12onos.tickets.ticket_purchase.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.k12onos.tickets.security.utils.SecurityUtil;
import app.k12onos.tickets.ticket_purchase.services.TicketPurchaseService;

@RestController
@RequestMapping(path = "/api/v1/ticket-types")
public class TicketTypeController {

    private final TicketPurchaseService ticketService;

    public TicketTypeController(TicketPurchaseService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{ticketTypeId}/tickets")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void purchaseTicket(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID ticketTypeId) {

        UUID userId = SecurityUtil.parseUserId(jwt);

        this.ticketService.purchaseTicket(userId, ticketTypeId);
    }

}
