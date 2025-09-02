package app.k12onos.tickets.security.exceptions;

import app.k12onos.tickets.base.exceptions.EventTicketException;

public class UserNotFoundException extends EventTicketException {
    public UserNotFoundException() {

    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(
            String message,
            Throwable cause,
            boolean enableSuppresion,
            boolean writeableStackTrace) {

        super(message, cause, enableSuppresion, writeableStackTrace);
    }
}
