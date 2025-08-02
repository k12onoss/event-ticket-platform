package app.k12onos.tickets.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import app.k12onos.tickets.domain.entities.QRCode;
import app.k12onos.tickets.exceptions.QRCodeNotFoundException;
import app.k12onos.tickets.repositories.QRCodeRepository;

@Service
public class QRCodeService {

    private final QRCodeRepository qrCodeRepository;

    public QRCodeService(QRCodeRepository qrCodeRepository) {
        this.qrCodeRepository = qrCodeRepository;
    }

    public QRCode getActiveQRCodeForTicketAndPurchaser(UUID ticketId, UUID purchaserId) {
        return qrCodeRepository
                .findActiveQRCodeForTicketAndPurchaser(ticketId, purchaserId)
                .orElseThrow(QRCodeNotFoundException::new);
    }

}
