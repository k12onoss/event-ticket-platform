package app.k12onos.tickets.ticket_management.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import app.k12onos.tickets.ticket.domain.entities.Ticket;
import app.k12onos.tickets.ticket.repositories.TicketRepository;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Page<Ticket> getTicketsForPurchaser(UUID purchaserId, Pageable pageable) {
        return this.ticketRepository.findTicketsForPurchaser(purchaserId, pageable);
    }

}
