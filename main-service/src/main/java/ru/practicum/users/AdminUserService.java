package ru.practicum.users;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotAvailableException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

/**
 * File Name: AdminUserService.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:35 PM (UTC+3)
 * Description:
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserService {
    private final UserRepository userRepository;

    @Transactional
    public UserFullDto add(NewUserDto newUserDto) {

        log.info("-- Сохранение пользователя:{}", newUserDto);

        // проверка
        if (userRepository.existsByEmail(newUserDto.getEmail())) {
            throw new NotAvailableException("Такой адрес почты уже есть в базе, пользователь не сохранён");
        }

        User user = UserMapper.newUserDtoToUser(newUserDto);

        UserFullDto fullUserDtoToReturn = UserMapper.userToFullDto(userRepository.save(user));

        log.info("-- Пользователь сохранён: {}", fullUserDtoToReturn);

        return fullUserDtoToReturn;
    }

    public List<UserFullDto> getByParams(Long[] userIds, int from, int size) {

        log.info("-- Возвращение пользователей с номерами:{}", Arrays.toString(userIds));

        // пагинация
        PageRequest pageRequest;

        if (size > 0 && from >= 0) {
            int page = from / size;
            pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
        } else {
            throw new ValidationException("- Размер страницы должен быть > 0, 'from' должен быть >= 0");
        }

        List<UserFullDto> listToReturn;

        // проверка userIds
        if (userIds == null) {
            listToReturn = UserMapper.userToFullDto(userRepository.findAll(pageRequest));
        } else {
            listToReturn = UserMapper.userToFullDto(userRepository.findByIdIn(userIds, pageRequest));
        }

        log.info("-- Список пользователей возвращен, его размер: {}", listToReturn.size());

        return listToReturn;
    }

    @Transactional
    public void removeById(Long userId) {

        log.info("--- Удаление пользователя №{}", userId);

        User userToCheck = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("- Пользователь №" + userId + " не найден в базе"));

        UserShortDto userToShowInLog = UserMapper.userToShortDto(userToCheck);

        userRepository.deleteById(userId);

        log.info("--- Пользователь удален: {}", userToShowInLog);
    }
}
