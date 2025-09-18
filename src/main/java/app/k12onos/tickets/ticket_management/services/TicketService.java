package app.k12onos.tickets.ticket_management.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import app.k12onos.tickets.event.services.S3Service;
import app.k12onos.tickets.ticket.domain.dtos.TicketDto;
import app.k12onos.tickets.ticket.repositories.TicketRepository;
import app.k12onos.tickets.ticket_management.domain.responses.TicketResponse;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final S3Service s3Service;

    public TicketService(TicketRepository ticketRepository, S3Service s3Service) {
        this.ticketRepository = ticketRepository;
        this.s3Service = s3Service;
    }

    public PagedModel<TicketResponse> getTicketsForPurchaser(UUID purchaserId, Pageable pageable) {
        Page<TicketDto> tickets = this.ticketRepository.findTicketsForPurchaser(purchaserId, pageable);

        Page<TicketResponse> responses = tickets.map(ticket -> {
            String posterUrl = this.s3Service.generateReadUrl(ticket.eventPosterKey());
            return TicketResponse.from(ticket, posterUrl);
        });

        return new PagedModel<>(responses);
    }

}
