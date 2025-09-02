package app.k12onos.tickets.event_management.exceptions;

import app.k12onos.tickets.base.exceptions.EventTicketException;

public class EventNotFoundException extends EventTicketException {
    public EventNotFoundException() {

    }

    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException(Throwable cause) {
        super(cause);
    }

    public EventNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventNotFoundException(
            String message,
            Throwable cause,
            boolean enableSuppresion,
            boolean writeableStackTrace) {

        super(message, cause, enableSuppresion, writeableStackTrace);
    }
}
