package app.k12onos.tickets.event_management.exceptions;

public class GetImageFailedException extends RuntimeException {

    public GetImageFailedException() {}

    public GetImageFailedException(String message) {
        super(message);
    }

    public GetImageFailedException(Throwable cause) {
        super(cause);
    }

    public GetImageFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GetImageFailedException(
        String message,
        Throwable cause,
        boolean enableSuppresion,
        boolean writeableStackTrace) {

        super(message, cause, enableSuppresion, writeableStackTrace);
    }

}
