package ru.practicum;

/**
 * File Name: ru.practicum.ValidationException.java
 * Author: Marina Volkova
 * Date: 2023-10-12,   10:44 PM (UTC+3)
 * Description:
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
