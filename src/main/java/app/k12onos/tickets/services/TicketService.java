package app.k12onos.tickets.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import app.k12onos.tickets.domain.entities.QRCode;
import app.k12onos.tickets.domain.entities.Ticket;
import app.k12onos.tickets.domain.entities.TicketType;
import app.k12onos.tickets.domain.entities.User;
import app.k12onos.tickets.domain.enums.QRCodeStatus;
import app.k12onos.tickets.domain.enums.TicketStatus;
import app.k12onos.tickets.exceptions.TicketTypeNotFoundException;
import app.k12onos.tickets.exceptions.TicketsSoldOutException;
import app.k12onos.tickets.exceptions.UserNotFoundException;
import app.k12onos.tickets.repositories.TicketRepository;
import app.k12onos.tickets.repositories.TicketTypeRepository;
import app.k12onos.tickets.repositories.UserRepository;
import app.k12onos.tickets.utils.TokenUtil;
import jakarta.transaction.Transactional;

@Service
public class TicketService {

    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;

    public TicketService(
            UserRepository userRepository,
            TicketTypeRepository ticketTypeRepository,
            TicketRepository ticketRepository) {

        this.userRepository = userRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Ticket purchaseTicket(UUID attendeeId, UUID ticketTypeId) {
        User attendee = this.userRepository
                .findById(attendeeId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + attendeeId + " not found"));

        TicketType ticketType = this.ticketTypeRepository
                .findByIdWithLock(ticketTypeId)
                .orElseThrow(
                        () -> new TicketTypeNotFoundException("Ticket type with id " + ticketTypeId + " not found"));

        int purchasedTicketsCount = this.ticketRepository.findCount(ticketTypeId);

        if (purchasedTicketsCount >= ticketType.getTotalAvailable()) {
            throw new TicketsSoldOutException("Tickets are sold out");
        }

        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatus.PURCHASED);
        ticket.setTicketType(ticketType);
        ticket.setPurchaser(attendee);

        QRCode qrCode = new QRCode();
        qrCode.setStatus(QRCodeStatus.ACTIVE);
        qrCode.setToken(TokenUtil.generateBase62Token());
        qrCode.setTicket(ticket);

        ticket.getQrCodes().add(qrCode);

        return this.ticketRepository.save(ticket);
    }

    public Page<Ticket> getTicketsForPurchaser(UUID purchaserId, Pageable pageable) {
        return this.ticketRepository.findTicketsForPurchaser(purchaserId, pageable);
    }

}
