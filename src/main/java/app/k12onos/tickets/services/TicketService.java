package app.k12onos.tickets.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import app.k12onos.tickets.domain.entities.Ticket;
import app.k12onos.tickets.domain.entities.TicketType;
import app.k12onos.tickets.domain.entities.User;
import app.k12onos.tickets.domain.enums.TicketStatus;
import app.k12onos.tickets.domain.responses.ListTicketResponse;
import app.k12onos.tickets.exceptions.TicketTypeNotFoundException;
import app.k12onos.tickets.exceptions.TicketsSoldOutException;
import app.k12onos.tickets.exceptions.UserNotFoundException;
import app.k12onos.tickets.repositories.TicketRepository;
import app.k12onos.tickets.repositories.TicketTypeRepository;
import app.k12onos.tickets.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class TicketService {

    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;

    private final QRCodeService qrCodeService;

    public TicketService(
            UserRepository userRepository,
            TicketTypeRepository ticketTypeRepository,
            TicketRepository ticketRepository,
            QRCodeService qrCodeService) {

        this.userRepository = userRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.ticketRepository = ticketRepository;
        this.qrCodeService = qrCodeService;
    }

    @Transactional
    public Ticket purchaseTicket(UUID attendeeId, UUID ticketTypeId) {
        User attendee = userRepository
                .findById(attendeeId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + attendeeId + " not found"));

        TicketType ticketType = ticketTypeRepository
                .findByIdWithLock(ticketTypeId)
                .orElseThrow(
                        () -> new TicketTypeNotFoundException("Ticket type with id " + ticketTypeId + " not found"));

        int purchasedTicketsCount = ticketRepository.findCount(ticketTypeId);

        if (purchasedTicketsCount >= ticketType.getTotalAvailable()) {
            throw new TicketsSoldOutException("Tickets are sold out");
        }

        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatus.PURCHASED);
        ticket.setTicketType(ticketType);
        ticket.setPurchaser(attendee);
        ticket.getQrCodes().add(qrCodeService.createQRCode(ticket));

        return ticketRepository.save(ticket);
    }

    public Page<ListTicketResponse> getTicketsForPurchaser(UUID purchaserId, Pageable pageable) {
        return ticketRepository.findTicketsForPurchaser(purchaserId, pageable);
    }

    public Optional<Ticket> getTicketForPurchaser(UUID purchaserId, UUID ticketId) {
        return ticketRepository.findTicketForPurchaser(purchaserId, ticketId);
    }

}
