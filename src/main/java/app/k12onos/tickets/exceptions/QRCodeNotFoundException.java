package app.k12onos.tickets.exceptions;

public class QRCodeNotFoundException extends EventTicketException {
    public QRCodeNotFoundException() {

    }

    public QRCodeNotFoundException(String message) {
        super(message);
    }

    public QRCodeNotFoundException(Throwable cause) {
        super(cause);
    }

    public QRCodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public QRCodeNotFoundException(
            String message,
            Throwable cause,
            boolean enableSuppresion,
            boolean writeableStackTrace) {

        super(message, cause, enableSuppresion, writeableStackTrace);
    }
}
