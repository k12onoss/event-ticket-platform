package app.k12onos.tickets.exceptions;

public class TicketsSoldOutException extends EventTicketException {

    public TicketsSoldOutException() {

    }

    public TicketsSoldOutException(String message) {
        super(message);
    }

    public TicketsSoldOutException(Throwable cause) {
        super(cause);
    }

    public TicketsSoldOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketsSoldOutException(
            String message,
            Throwable cause,
            boolean enableSuppresion,
            boolean writeableStackTrace) {

        super(message, cause, enableSuppresion, writeableStackTrace);
    }

}
