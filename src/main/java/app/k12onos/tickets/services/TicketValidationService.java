package app.k12onos.tickets.services;

import org.springframework.stereotype.Service;

import app.k12onos.tickets.domain.QRPayload;
import app.k12onos.tickets.domain.entities.QRCode;
import app.k12onos.tickets.domain.entities.Ticket;
import app.k12onos.tickets.domain.entities.TicketValidation;
import app.k12onos.tickets.domain.enums.QRCodeStatus;
import app.k12onos.tickets.domain.enums.TicketValidationStatus;
import app.k12onos.tickets.domain.requests.TicketValidationRequest;
import app.k12onos.tickets.exceptions.QRCodeNotFoundException;
import app.k12onos.tickets.repositories.QRCodeRepository;
import app.k12onos.tickets.repositories.TicketValidationRepository;

@Service
public class TicketValidationService {

    private final QRCodeRepository qrCodeRepository;
    private final TicketValidationRepository ticketValidationRepository;

    public TicketValidationService(
            QRCodeRepository qrCodeRepository,
            TicketValidationRepository ticketValidationRepository) {

        this.qrCodeRepository = qrCodeRepository;
        this.ticketValidationRepository = ticketValidationRepository;
    }

    public TicketValidation validateTicket(TicketValidationRequest validationRequest) {
        QRPayload payload = QRPayload.decodeFromBase64(validationRequest.value());

        QRCode qrCode = qrCodeRepository
                .findById(payload.qrCodeId())
                .orElseThrow(QRCodeNotFoundException::new);

        Ticket ticket = qrCode.getTicket();
        long validValidationsCount = ticketValidationRepository.countValidValidationsByTicket(ticket.getId());

        TicketValidationStatus status = qrCode.getStatus() == QRCodeStatus.ACTIVE && validValidationsCount == 0L
                ? TicketValidationStatus.VALID
                : TicketValidationStatus.INVALID;

        TicketValidation validation = new TicketValidation();
        validation.setStatus(status);
        validation.setValidationMethod(validationRequest.validationMethod());
        validation.setTicket(ticket);

        return ticketValidationRepository.save(validation);
    }

}
