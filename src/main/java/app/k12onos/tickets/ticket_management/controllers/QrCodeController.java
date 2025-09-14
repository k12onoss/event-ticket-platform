package app.k12onos.tickets.ticket_management.controllers;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.k12onos.tickets.security.utils.SecurityUtil;
import app.k12onos.tickets.ticket_management.domain.responses.QrCodeResponse;
import app.k12onos.tickets.ticket_management.services.QrCodeService;

@RestController
@RequestMapping(path = "/api/v1/tickets/{ticketId}/qr-codes")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping
    QrCodeResponse getActiveQrCode(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID ticketId) {
        UUID userId = SecurityUtil.parseUserId(jwt);

        return this.qrCodeService.getActiveQrCodeForTicketAndPurchaser(ticketId, userId);
    }

}
