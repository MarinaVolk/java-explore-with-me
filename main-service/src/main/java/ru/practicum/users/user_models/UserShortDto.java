package ru.practicum.users.user_models;/* # parse("File Header.java")*/

import lombok.Data;

@Data
public class UserShortDto {
    private final Long id;
    private final String name;
}
