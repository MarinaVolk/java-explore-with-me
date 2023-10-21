package ru.practicum.exceptions;/* # parse("File Header.java")*/

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.Map;

/**
 * File Name: ErrorHandler.java
 * Author: Marina Volkova
 * Date: 2023-10-19,   9:21 PM (UTC+3)
 * Description:
 */

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorResponse handleNotFoundException(final NotFoundException e) {

        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponse handleIncorrectException(final ValidationException e) {

        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public ErrorResponse handleConflictException(final NotAvailableException e) {

        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(NotFoundException e) {
        log.error("Код ошибки: {}, {}", HttpStatus.NOT_FOUND, e.getMessage());
        return Map.of(
                "status", "NOT FOUND",
                "reason", "Object has not found",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(Throwable e) {
        log.error("Error: {}, {}", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return Map.of(
                "status", "INTERNAL SERVER ERROR",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotValidException(MethodArgumentNotValidException e) {
        log.error("Код ошибки: {}, {}", HttpStatus.BAD_REQUEST, e.getMessage());
        return Map.of(
                "status", "BAD REQUEST",
                "reason", "Request isn't correct",
                "message", e.getMessage()
        );
    }

    /*@ExceptionHandler({MethodArgumentTypeMismatchException.class, ConstraintViolationException.class,
            MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotValidException(final RuntimeException e) {
        log.error("Код ошибки: {}, {}", HttpStatus.BAD_REQUEST, e.getMessage());
        return Map.of(
                "status", "BAD REQUEST",
                "reason", "Request isn't correct",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleConstraintViolationException(RuntimeException e) {
        log.error("Код ошибки: {}, {}", HttpStatus.CONFLICT, e.getMessage());
        return Map.of(
                "status", "CONFLICT",
                "reason", "Constraint Violation Exception",
                "message", e.getMessage()
        );
    }
*/
    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleConstraintViolationException(NotAvailableException e) {
        log.error("Код ошибки: {}, {}", HttpStatus.CONFLICT, e.getMessage());
        return Map.of(
                "status", "CONFLICT",
                "reason", "Constraint Violation Exception",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleViolationDateException(ValidationException e) {
        log.error("Код ошибки: {}, {}", HttpStatus.BAD_REQUEST, e.getMessage());
        return Map.of(
                "status", "BAD REQUEST",
                "reason", "Request conditions aren't correct",
                "message", e.getMessage()
        );
    }  */

}