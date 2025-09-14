package app.k12onos.tickets.event.exceptions;

public class DeleteFileFailedException extends RuntimeException {

    public DeleteFileFailedException() {}

    public DeleteFileFailedException(String message) {
        super(message);
    }

    public DeleteFileFailedException(Throwable cause) {
        super(cause);
    }

    public DeleteFileFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteFileFailedException(
        String message,
        Throwable cause,
        boolean enableSuppresion,
        boolean writeableStackTrace) {

        super(message, cause, enableSuppresion, writeableStackTrace);
    }

}
