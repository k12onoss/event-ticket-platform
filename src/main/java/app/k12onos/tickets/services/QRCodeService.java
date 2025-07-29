package app.k12onos.tickets.services;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import app.k12onos.tickets.domain.entities.QRCode;
import app.k12onos.tickets.domain.entities.Ticket;
import app.k12onos.tickets.domain.enums.QRCodeStatus;
import app.k12onos.tickets.exceptions.QRCodeNotFoundException;
import app.k12onos.tickets.repositories.QRCodeRepository;

@Service
public class QRCodeService {

    private final QRCodeRepository qrCodeRepository;

    public QRCodeService(QRCodeRepository qrCodeRepository) {
        this.qrCodeRepository = qrCodeRepository;
    }

    public QRCode createQRCode(Ticket ticket) {
        QRCode qrCode = new QRCode();
        qrCode.setStatus(QRCodeStatus.ACTIVE);
        qrCode.setValue(generatePayloadValue(qrCode.getId(), ticket.getId()));
        qrCode.setTicket(ticket);

        return qrCode;
    }

    public QRCode getActiveQRCodeForTicketAndPurchaser(UUID ticketId, UUID purchaserId) {
        return qrCodeRepository
                .findActiveQRCodeForTicketAndPurchaser(ticketId, purchaserId)
                .orElseThrow(QRCodeNotFoundException::new);
    }

    private String generatePayloadValue(UUID qrCodeId, UUID ticketId) {
        Map<String, Object> payload = Map.of(
                "qrCodeId", qrCodeId,
                "ticketId", ticketId);

        return Base64
                .getUrlEncoder()
                .withoutPadding()
                .encodeToString(payload.toString().getBytes());
    }

}
