package ru.practicum.category.category_models;/* # parse("File Header.java")*/

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
