package app.k12onos.tickets.event.exceptions;

public class GeneratePresignedUrlFailedException extends RuntimeException {

    public GeneratePresignedUrlFailedException() {}

    public GeneratePresignedUrlFailedException(String message) {
        super(message);
    }

    public GeneratePresignedUrlFailedException(Throwable cause) {
        super(cause);
    }

    public GeneratePresignedUrlFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneratePresignedUrlFailedException(
        String message,
        Throwable cause,
        boolean enableSuppresion,
        boolean writeableStackTrace) {

        super(message, cause, enableSuppresion, writeableStackTrace);
    }

}
