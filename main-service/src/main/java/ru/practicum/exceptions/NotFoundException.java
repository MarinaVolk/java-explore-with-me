package ru.practicum.exceptions;/* # parse("File Header.java")*/

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
