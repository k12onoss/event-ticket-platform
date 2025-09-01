package app.k12onos.tickets.domain.responses;

import app.k12onos.tickets.domain.entities.QRCode;
import app.k12onos.tickets.utils.TokenUtil;

public record QRCodeResponse(String token, String signature) {

    public QRCodeResponse(String token) {
        this(token, TokenUtil.signToken(token));
    }

    public static QRCodeResponse from(QRCode qrCode) {
        final String signature = TokenUtil.signToken(qrCode.getToken());
        return new QRCodeResponse(qrCode.getToken(), signature);
    }

}
