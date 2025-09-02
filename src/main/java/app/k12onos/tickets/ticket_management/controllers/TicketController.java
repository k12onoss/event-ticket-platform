package app.k12onos.tickets.ticket_management.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.k12onos.tickets.ticket_management.domain.responses.TicketResponse;
import app.k12onos.tickets.ticket_management.services.TicketService;

@RestController
@RequestMapping(path = "/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    PagedModel<TicketResponse> getTickets(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable) {

        UUID userId = this.parseUserId(jwt);

        Page<TicketResponse> tickets = this.ticketService
                .getTicketsForPurchaser(userId, pageable)
                .map(TicketResponse::from);

        return new PagedModel<>(tickets);
    }

    private UUID parseUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

}
