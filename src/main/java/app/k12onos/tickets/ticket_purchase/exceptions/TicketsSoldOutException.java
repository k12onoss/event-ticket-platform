package app.k12onos.tickets.ticket_purchase.exceptions;

public class TicketsSoldOutException extends RuntimeException {

    public TicketsSoldOutException() {}

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
