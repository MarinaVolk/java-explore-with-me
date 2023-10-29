package ru.practicum.users.user_models;/* # parse("File Header.java")*/

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static User newUserDtoToUser(NewUserDto newUserDto) {

        User user = new User();
        user.setName(newUserDto.getName());
        user.setEmail(newUserDto.getEmail());
        return user;
    }

    public static UserFullDto userToFullDto(User user) {

        return new UserFullDto(user.getId(), user.getName(), user.getEmail());
    }

    public static List<UserFullDto> userToFullDto(Iterable<User> users) {

        List<UserFullDto> toReturn = new ArrayList<>();

        for (User user : users) {
            toReturn.add(userToFullDto(user));
        }
        return toReturn;
    }

    public static UserShortDto userToShortDto(User user) {

        return new UserShortDto(user.getId(), user.getName());
    }
}
