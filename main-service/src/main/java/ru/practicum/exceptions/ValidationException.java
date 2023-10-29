package ru.practicum.exceptions;/* # parse("File Header.java")*/

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
