package app.k12onos.tickets.controllers;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.k12onos.tickets.domain.entities.QRCode;
import app.k12onos.tickets.domain.responses.QRCodeResponse;
import app.k12onos.tickets.services.QRCodeService;

@RestController
@RequestMapping(path = "/api/v1/tickets/{ticketId}/qr-codes")
public class QRCodeController {

    private final QRCodeService qrCodeService;

    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping
    QRCodeResponse getActiveQRCode(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID ticketId) {

        UUID userId = parseUserId(jwt);

        QRCode qrCode = qrCodeService.getActiveQRCodeForTicketAndPurchaser(ticketId, userId);

        return QRCodeResponse.from(qrCode);
    }

    private UUID parseUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

}