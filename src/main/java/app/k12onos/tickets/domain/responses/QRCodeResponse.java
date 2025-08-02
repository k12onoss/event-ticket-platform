package app.k12onos.tickets.domain.responses;

import app.k12onos.tickets.domain.entities.QRCode;

public record QRCodeResponse(String value) {

    public static QRCodeResponse from(QRCode qrCode) {
        return new QRCodeResponse(qrCode.getValue());
    }

}
