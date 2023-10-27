package ru.practicum.compilations.compilations_models;/* # parse("File Header.java")*/

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * File Name: NewCompilationDto.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   5:30 PM (UTC+3)
 * Description:
 */

@Data
public class NewCompilationDto {
    private final Long[] events;

    private final Boolean pinned;

    @NotNull
    @NotBlank
    @Size(min = 1)
    @Size(max = 50)
    private final String title;
}
