package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    private final UserService userService;

    //ToDo Получение информации о пользователях
    //Возвращает информацию обо всех пользователях (учитываются параметры ограничения выборки), либо о конкретных (учитываются указанные идентификаторы)
    //В случае, если по заданным фильтрам не найдено ни одного пользователя, возвращает пустой список
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAdminAllUsers(@RequestParam(required = false) List<Long> ids, //список id пользователей
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        log.info("Входящий запрос GET /admin/users?ids={}&from={}&size={}.", ids, from, size);
        final List<UserDto> userDtoList = userService.getAdminAllUsers(ids, from, size);
        log.info("Исходящий ответ: {}", userDtoList);
        return userDtoList;
    }

    //ToDo Добавление нового пользователя
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto crateUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("Входящий запрос POST /admin/users: {}", newUserRequest);
        final UserDto userDto = userService.createUser(newUserRequest);
        log.info("Исходящий ответ: {}", userDto);
        return userDto;
    }

    //ToDo Удаление пользователя
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long userId) {
        log.info("Входящий запрос DELETE /admin/users: {}", userId);
        userService.deleteUser(userId);
    }
}
