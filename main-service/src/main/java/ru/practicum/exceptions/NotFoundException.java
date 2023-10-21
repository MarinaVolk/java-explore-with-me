package ru.practicum.exceptions;/* # parse("File Header.java")*/

/**
 * File Name: NotFoundException.java
 * Author: Marina Volkova
 * Date: 2023-10-19,   9:18 PM (UTC+3)
 * Description:
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
