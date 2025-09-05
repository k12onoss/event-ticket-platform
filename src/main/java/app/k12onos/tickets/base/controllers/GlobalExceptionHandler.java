package app.k12onos.tickets.base.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import app.k12onos.tickets.base.domain.responses.ErrorResponse;
import app.k12onos.tickets.event_management.exceptions.EventNotFoundException;
import app.k12onos.tickets.event_management.exceptions.TicketTypeNotFoundException;
import app.k12onos.tickets.security.exceptions.UserNotFoundException;
import app.k12onos.tickets.ticket.exceptions.QrCodeGenerationException;
import app.k12onos.tickets.ticket.exceptions.QrCodeNotFoundException;
import app.k12onos.tickets.ticket_purchase.exceptions.TicketsSoldOutException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(UserNotFoundException ex) {
        LOG.error("Caught UserNotFoundException " + ex.getMessage());

        return new ErrorResponse("User not found");
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(EventNotFoundException ex) {
        LOG.error("Caught EventNotFoundException " + ex.getMessage());

        return new ErrorResponse("Event not found");
    }

    @ExceptionHandler(TicketTypeNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(TicketTypeNotFoundException ex) {
        LOG.error("Caught TicketTypeNotFoundException " + ex.getMessage());

        return new ErrorResponse("TicketType not found");
    }

    @ExceptionHandler(QrCodeNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(QrCodeNotFoundException ex) {
        LOG.error("Caught QrCodeNotFoundException " + ex.getMessage());

        return new ErrorResponse("QR code not found");
    }

    @ExceptionHandler(TicketsSoldOutException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(TicketsSoldOutException ex) {
        LOG.error("Caught TicketsSoldOutException " + ex.getMessage());

        return new ErrorResponse("Tickets are sold out");
    }

    @ExceptionHandler(QrCodeGenerationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(QrCodeGenerationException ex) {
        LOG.error("Caught QrCodeGenerationException " + ex.getMessage());

        return new ErrorResponse("Error generating QR code");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(MethodArgumentNotValidException ex) {
        LOG.error("Caught MethodArgumentNotValidException " + ex.getMessage());

        String message = ex
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .orElse("Validation error occured");
        return new ErrorResponse(message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(ConstraintViolationException ex) {
        LOG.error("Caught ConstraintViolationException " + ex.getMessage());

        String message = ex
            .getConstraintViolations()
            .stream()
            .findFirst()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .orElse("Constraint violation occured");
        return new ErrorResponse(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        LOG.error("Caught exception", ex);

        return new ErrorResponse("An unknown error occured");
    }

}
