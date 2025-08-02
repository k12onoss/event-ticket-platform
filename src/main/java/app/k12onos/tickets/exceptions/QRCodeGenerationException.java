package app.k12onos.tickets.exceptions;

public class QRCodeGenerationException extends EventTicketException {

    public QRCodeGenerationException() {

    }

    public QRCodeGenerationException(String message) {
        super(message);
    }

    public QRCodeGenerationException(Throwable cause) {
        super(cause);
    }

    public QRCodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public QRCodeGenerationException(
            String message,
            Throwable cause,
            boolean enableSuppresion,
            boolean writeableStackTrace) {

        super(message, cause, enableSuppresion, writeableStackTrace);
    }

}
