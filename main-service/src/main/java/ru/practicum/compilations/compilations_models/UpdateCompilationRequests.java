package ru.practicum.compilations.compilations_models;/* # parse("File Header.java")*/

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * File Name: UpdateCompilationRequests.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   5:30 PM (UTC+3)
 * Description:
 */

@Data
public class UpdateCompilationRequests {
    private final Long[] events;

    private final Boolean pinned;

    @Size(min = 1, message = "size must be between 1 and 50")
    @Size(max = 50, message = "size must be between 1 and 50")
    private final String title;
}
