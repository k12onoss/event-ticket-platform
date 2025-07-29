package app.k12onos.tickets.controllers;

import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import app.k12onos.tickets.domain.responses.ListTicketResponse;
import app.k12onos.tickets.domain.responses.TicketResponse;
import app.k12onos.tickets.services.TicketService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping(path = "/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    PagedModel<ListTicketResponse> getTickets(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable) {

        UUID userId = parseUserId(jwt);

        Page<ListTicketResponse> tickets = ticketService.getTicketsForPurchaser(userId, pageable);

        return new PagedModel<>(tickets);
    }

    @GetMapping("/{ticketId}")
    ResponseEntity<TicketResponse> getTicket(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID ticketId) {

        UUID userId = parseUserId(jwt);

        return ticketService
                .getTicketForPurchaser(userId, ticketId)
                .map(TicketResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    private UUID parseUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

}
