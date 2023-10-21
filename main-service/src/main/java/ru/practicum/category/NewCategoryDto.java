package ru.practicum.category;/* # parse("File Header.java")*/

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * File Name: NewCategoryDto.java
 * Author: Marina Volkova
 * Date: 2023-10-19,   8:55 PM (UTC+3)
 * Description:
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewCategoryDto {
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 1, max = 50)
    private String name;
}
