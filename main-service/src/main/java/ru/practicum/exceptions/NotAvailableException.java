package ru.practicum.exceptions;/* # parse("File Header.java")*/

public class NotAvailableException extends RuntimeException {
    public NotAvailableException(String message) {
        super(message);
    }
}
