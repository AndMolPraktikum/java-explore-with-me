package ru.practicum.user.mapper;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toUserEntity(NewUserRequest newUserRequest) {
        return new User(
                newUserRequest.getName(),
                newUserRequest.getEmail()
        );
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }

    public static List<UserDto> toUserDtoList(List<User> userList) {
        return userList.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
