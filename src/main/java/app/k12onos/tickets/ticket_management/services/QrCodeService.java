package app.k12onos.tickets.ticket_management.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import app.k12onos.tickets.ticket.domain.entities.QrCode;
import app.k12onos.tickets.ticket.exceptions.QrCodeNotFoundException;
import app.k12onos.tickets.ticket.repositories.QrCodeRepository;

@Service
public class QrCodeService {

    private final QrCodeRepository qrCodeRepository;

    public QrCodeService(QrCodeRepository qrCodeRepository) {
        this.qrCodeRepository = qrCodeRepository;
    }

    public QrCode getActiveQrCodeForTicketAndPurchaser(UUID ticketId, UUID purchaserId) {
        return this.qrCodeRepository
            .findActiveQrCodeForTicketAndPurchaser(ticketId, purchaserId)
            .orElseThrow(QrCodeNotFoundException::new);
    }

}
