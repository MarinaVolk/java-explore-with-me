package ru.practicum.exceptions;/* # parse("File Header.java")*/

/**
 * File Name: ValidationException.java
 * Author: Marina Volkova
 * Date: 2023-10-19,   9:19 PM (UTC+3)
 * Description:
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
