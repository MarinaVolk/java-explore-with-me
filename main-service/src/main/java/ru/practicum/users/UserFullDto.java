package ru.practicum.users;/* # parse("File Header.java")*/

import lombok.Data;

/**
 * File Name: UserFullDto.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:32 PM (UTC+3)
 * Description:
 */

@Data
public class UserFullDto implements Comparable<UserFullDto> {
    private final Long id;

    private final String name;

    private final String email;

    @Override
    public int compareTo(UserFullDto o) {
        return Long.compare(this.id, o.id);
    }
}
