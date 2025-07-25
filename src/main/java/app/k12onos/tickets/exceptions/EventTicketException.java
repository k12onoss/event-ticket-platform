package app.k12onos.tickets.exceptions;

public class EventTicketException extends RuntimeException {

    public EventTicketException() {

    }

    public EventTicketException(String message) {
        super(message);
    }

    public EventTicketException(Throwable cause) {
        super(cause);
    }

    public EventTicketException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventTicketException(
            String message,
            Throwable cause,
            boolean enableSuppresion,
            boolean writeableStackTrace) {

        super(message, cause, enableSuppresion, writeableStackTrace);
    }

}
