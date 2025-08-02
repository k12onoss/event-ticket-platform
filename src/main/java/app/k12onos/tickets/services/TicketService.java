package app.k12onos.tickets.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import app.k12onos.tickets.domain.QRPayload;
import app.k12onos.tickets.domain.entities.QRCode;
import app.k12onos.tickets.domain.entities.Ticket;
import app.k12onos.tickets.domain.entities.TicketType;
import app.k12onos.tickets.domain.entities.User;
import app.k12onos.tickets.domain.enums.QRCodeStatus;
import app.k12onos.tickets.domain.enums.TicketStatus;
import app.k12onos.tickets.exceptions.TicketTypeNotFoundException;
import app.k12onos.tickets.exceptions.TicketsSoldOutException;
import app.k12onos.tickets.exceptions.UserNotFoundException;
import app.k12onos.tickets.repositories.QRCodeRepository;
import app.k12onos.tickets.repositories.TicketRepository;
import app.k12onos.tickets.repositories.TicketTypeRepository;
import app.k12onos.tickets.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class TicketService {

    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final QRCodeRepository qrCodeRepository;

    public TicketService(
            UserRepository userRepository,
            TicketTypeRepository ticketTypeRepository,
            TicketRepository ticketRepository,
            QRCodeRepository qrCodeRepository) {

        this.userRepository = userRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.ticketRepository = ticketRepository;
        this.qrCodeRepository = qrCodeRepository;
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

        QRCode qrCode = new QRCode();
        qrCode.setStatus(QRCodeStatus.ACTIVE);
        qrCode.setTicket(ticket);

        ticket.getQrCodes().add(qrCode);

        Ticket createdTicket = ticketRepository.save(ticket);

        QRPayload payload = new QRPayload(qrCode.getId(), ticket.getId());
        qrCode.setValue(payload.encodeToBase64());

        qrCodeRepository.save(qrCode);

        return createdTicket;
    }

    public Page<Ticket> getTicketsForPurchaser(UUID purchaserId, Pageable pageable) {
        return ticketRepository.findTicketsForPurchaser(purchaserId, pageable);
    }

    public Optional<Ticket> getTicketForPurchaser(UUID purchaserId, UUID ticketId) {
        return ticketRepository.findTicketForPurchaser(purchaserId, ticketId);
    }

}
