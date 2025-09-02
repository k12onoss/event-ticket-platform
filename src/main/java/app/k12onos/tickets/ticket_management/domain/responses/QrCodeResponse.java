package app.k12onos.tickets.ticket_management.domain.responses;

import app.k12onos.tickets.ticket.domain.entities.QrCode;
import app.k12onos.tickets.ticket.utils.TokenUtil;

public record QrCodeResponse(String token, String signature) {

    public QrCodeResponse(String token) {
        this(token, TokenUtil.signToken(token));
    }

    public static QrCodeResponse from(QrCode qrCode) {
        final String signature = TokenUtil.signToken(qrCode.getToken());
        return new QrCodeResponse(qrCode.getToken(), signature);
    }

}
