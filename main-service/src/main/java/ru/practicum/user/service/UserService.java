package ru.practicum.user.service;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserService {

    User getUserById(Long userId);

    List<UserDto> getAdminAllUsers(List<Long> ids, int from, int size);

    UserDto createUser(NewUserRequest newUserRequest);

    void deleteUser(Long userId);
}
