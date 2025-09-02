package app.k12onos.tickets.ticket.exceptions;

import app.k12onos.tickets.base.exceptions.EventTicketException;

public class QrCodeNotFoundException extends EventTicketException {
    public QrCodeNotFoundException() {

    }

    public QrCodeNotFoundException(String message) {
        super(message);
    }

    public QrCodeNotFoundException(Throwable cause) {
        super(cause);
    }

    public QrCodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public QrCodeNotFoundException(
            String message,
            Throwable cause,
            boolean enableSuppresion,
            boolean writeableStackTrace) {

        super(message, cause, enableSuppresion, writeableStackTrace);
    }
}
