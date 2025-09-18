package app.k12onos.tickets.ticket_management.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import app.k12onos.tickets.ticket.domain.entities.QrCode;
import app.k12onos.tickets.ticket.domain.enums.QrCodeStatus;
import app.k12onos.tickets.ticket.exceptions.QrCodeNotFoundException;
import app.k12onos.tickets.ticket.repositories.QrCodeRepository;
import app.k12onos.tickets.ticket.services.TokenService;
import app.k12onos.tickets.ticket_management.domain.responses.QrCodeResponse;

@Service
public class QrCodeService {

    private final QrCodeRepository qrCodeRepository;
    private final TokenService tokenService;

    public QrCodeService(QrCodeRepository qrCodeRepository, TokenService tokenService) {
        this.qrCodeRepository = qrCodeRepository;
        this.tokenService = tokenService;
    }

    public QrCodeResponse getActiveQrCodeForTicketAndPurchaser(UUID ticketId, UUID purchaserId) {
        QrCode qrCode = this.qrCodeRepository
            .findQrCodeForTicketAndPurchaserAndStatus(ticketId, purchaserId, QrCodeStatus.ACTIVE)
            .orElseThrow(QrCodeNotFoundException::new);

        return new QrCodeResponse(qrCode.getToken(), this.tokenService.signToken(qrCode.getToken()));
    }

}
