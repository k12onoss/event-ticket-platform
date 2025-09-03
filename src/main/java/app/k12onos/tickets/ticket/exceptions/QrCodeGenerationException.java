package app.k12onos.tickets.ticket.exceptions;

public class QrCodeGenerationException extends RuntimeException {

    public QrCodeGenerationException() {}

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
