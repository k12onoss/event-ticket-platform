package app.k12onos.tickets.ticket.exceptions;

import app.k12onos.tickets.base.exceptions.EventTicketException;

public class QrCodeGenerationException extends EventTicketException {

    public QrCodeGenerationException() {

    }

    public QrCodeGenerationException(String message) {
        super(message);
    }

    public QrCodeGenerationException(Throwable cause) {
        super(cause);
    }

    public QrCodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public QrCodeGenerationException(
            String message,
            Throwable cause,
            boolean enableSuppresion,
            boolean writeableStackTrace) {

        super(message, cause, enableSuppresion, writeableStackTrace);
    }

}
