package ru.practicum.users.user_logic;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.user_models.NewUserDto;
import ru.practicum.users.user_models.UserFullDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * File Name: AdminUserController.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:34 PM (UTC+3)
 * Description:
 */

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class AdminUserController {
    private final AdminUserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserFullDto add(@RequestBody @Valid NewUserDto newUserDto) {

        return userService.add(newUserDto);
    }

    @GetMapping
    public List<UserFullDto> getByParams(@RequestParam(value = "ids", required = false) Long[] userIds,
                                         @RequestParam(value = "from", defaultValue = "0") int from,
                                         @RequestParam(value = "size", defaultValue = "10") int size) {

        return userService.getByParams(userIds, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeById(@PathVariable @NotNull Long userId) {

        userService.removeById(userId);
    }
}
