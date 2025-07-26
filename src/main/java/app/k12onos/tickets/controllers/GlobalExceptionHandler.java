package app.k12onos.tickets.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import app.k12onos.tickets.domain.responses.ErrorResponse;
import app.k12onos.tickets.exceptions.EventNotFoundException;
import app.k12onos.tickets.exceptions.TicketTypeNotFoundException;
import app.k12onos.tickets.exceptions.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(UserNotFoundException ex) {
        logger.error("Caught UserNotFoundException " + ex.getMessage());

        ErrorResponse error = new ErrorResponse("User not found");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(EventNotFoundException ex) {
        logger.error("Caught EventNotFoundException " + ex.getMessage());

        ErrorResponse error = new ErrorResponse("Event not found");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TicketTypeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(TicketTypeNotFoundException ex) {
        logger.error("Caught TicketTypeNotFoundException " + ex.getMessage());

        ErrorResponse error = new ErrorResponse("TicketType not found");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException ex) {
        logger.error("Caught MethodArgumentNotValidException " + ex.getMessage());

        String message = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .orElse("Validation error occured");
        ErrorResponse error = new ErrorResponse(message);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleException(ConstraintViolationException ex) {
        logger.error("Caught ConstraintViolationException " + ex.getMessage());

        String message = ex
                .getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .orElse("Constraint violation occured");
        ErrorResponse error = new ErrorResponse(message);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        logger.error("Caught exception", ex);

        ErrorResponse error = new ErrorResponse("An unknown error occured");

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
