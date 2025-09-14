package app.k12onos.tickets.event_management.exceptions;

public class UploadImageFailedException extends RuntimeException {

    public UploadImageFailedException() {}

    public UploadImageFailedException(String message) {
        super(message);
    }

    public UploadImageFailedException(Throwable cause) {
        super(cause);
    }

    public UploadImageFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadImageFailedException(
        String message,
        Throwable cause,
        boolean enableSuppresion,
        boolean writeableStackTrace) {

        super(message, cause, enableSuppresion, writeableStackTrace);
    }

}
