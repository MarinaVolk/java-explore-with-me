package ru.practicum.exceptions;/* # parse("File Header.java")*/

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

}