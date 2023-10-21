package ru.practicum.exceptions;/* # parse("File Header.java")*/

/**
 * File Name: ErrorResponse.java
 * Author: Marina Volkova
 * Date: 2023-10-21,   10:57 PM (UTC+3)
 * Description:
 */

public class ErrorResponse {

    String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
