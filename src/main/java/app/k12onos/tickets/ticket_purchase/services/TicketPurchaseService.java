package app.k12onos.tickets.ticket_purchase.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import app.k12onos.tickets.event.domain.entities.TicketType;
import app.k12onos.tickets.event_management.exceptions.TicketTypeNotFoundException;
import app.k12onos.tickets.security.domain.entities.User;
import app.k12onos.tickets.security.exceptions.UserNotFoundException;
import app.k12onos.tickets.security.repositories.UserRepository;
import app.k12onos.tickets.ticket.domain.entities.QrCode;
import app.k12onos.tickets.ticket.domain.entities.Ticket;
import app.k12onos.tickets.ticket.domain.enums.QrCodeStatus;
import app.k12onos.tickets.ticket.domain.enums.TicketStatus;
import app.k12onos.tickets.ticket.repositories.TicketRepository;
import app.k12onos.tickets.ticket.utils.TokenUtil;
import app.k12onos.tickets.ticket_purchase.exceptions.TicketsSoldOutException;
import app.k12onos.tickets.ticket_purchase.repositories.TicketTypeRepository;
import jakarta.transaction.Transactional;

@Service
public class TicketPurchaseService {

    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;

    public TicketPurchaseService(UserRepository userRepository, TicketTypeRepository ticketTypeRepository,
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

        QrCode qrCode = new QrCode();
        qrCode.setStatus(QrCodeStatus.ACTIVE);
        qrCode.setToken(TokenUtil.generateBase62Token());
        qrCode.setTicket(ticket);

        ticket.getQrCodes().add(qrCode);

        return this.ticketRepository.save(ticket);
    }

}
