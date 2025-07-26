package app.k12onos.tickets.exceptions;

public class TicketTypeNotFoundException extends EventTicketException {
    public TicketTypeNotFoundException() {

    }

    public TicketTypeNotFoundException(String message) {
        super(message);
    }

    public TicketTypeNotFoundException(Throwable cause) {
        super(cause);
    }

    public TicketTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketTypeNotFoundException(
            String message,
            Throwable cause,
            boolean enableSuppresion,
            boolean writeableStackTrace) {

        super(message, cause, enableSuppresion, writeableStackTrace);
    }
}
