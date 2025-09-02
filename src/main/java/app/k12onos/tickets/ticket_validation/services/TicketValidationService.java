package app.k12onos.tickets.ticket_validation.services;

import org.springframework.stereotype.Service;

import app.k12onos.tickets.ticket.domain.entities.QrCode;
import app.k12onos.tickets.ticket.domain.entities.Ticket;
import app.k12onos.tickets.ticket.domain.enums.QrCodeStatus;
import app.k12onos.tickets.ticket.exceptions.QrCodeNotFoundException;
import app.k12onos.tickets.ticket.repositories.QrCodeRepository;
import app.k12onos.tickets.ticket_validation.domain.entities.TicketValidation;
import app.k12onos.tickets.ticket_validation.domain.enums.TicketValidationStatus;
import app.k12onos.tickets.ticket_validation.domain.requests.TicketValidationRequest;
import app.k12onos.tickets.ticket_validation.repositories.TicketValidationRepository;
import jakarta.transaction.Transactional;

@Service
public class TicketValidationService {

    private final QrCodeRepository qrCodeRepository;
    private final TicketValidationRepository ticketValidationRepository;

    public TicketValidationService(
            QrCodeRepository qrCodeRepository,
            TicketValidationRepository ticketValidationRepository) {

        this.qrCodeRepository = qrCodeRepository;
        this.ticketValidationRepository = ticketValidationRepository;
    }

    @Transactional
    public TicketValidation validateTicket(TicketValidationRequest validationRequest) {
        QrCode qrCode = this.qrCodeRepository
                .findByToken(validationRequest.token())
                .orElseThrow(QrCodeNotFoundException::new);

        Ticket ticket = qrCode.getTicket();
        long validValidationsCount = this.ticketValidationRepository.countValidValidationsByTicket(ticket.getId());

        TicketValidationStatus status = qrCode.getStatus() == QrCodeStatus.ACTIVE && validValidationsCount == 0L
                ? TicketValidationStatus.VALID
                : TicketValidationStatus.INVALID;

        TicketValidation validation = new TicketValidation();
        validation.setStatus(status);
        validation.setValidationMethod(validationRequest.validationMethod());
        validation.setTicket(ticket);

        return this.ticketValidationRepository.save(validation);
    }

}
