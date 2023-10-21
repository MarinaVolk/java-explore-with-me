package ru.practicum.exceptions;/* # parse("File Header.java")*/

/**
 * File Name: NotAvailableException.java
 * Author: Marina Volkova
 * Date: 2023-10-19,   9:23 PM (UTC+3)
 * Description:
 */
public class NotAvailableException extends RuntimeException {
    public NotAvailableException(String message) {
        super(message);
    }
}
