package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            log.error("user with id={} was not found", userId);
            throw new UserNotFoundException(String.format("Category with id=%d was not found", userId));
        }
        return userOptional.get();
    }

    @Override
    public List<UserDto> getAdminAllUsers(List<Long> ids, int from, int size) {
        Pageable page = PageRequest.of(from, size, Sort.by("id"));
        List<User> userList;
        if (ids == null) {
            userList = userRepository.findAll(page).getContent();
        } else {
            userList = userRepository.findAllByIdIn(ids, page);
        }
        return UserMapper.toUserDtoList(userList);
    }

    @Override
    @Transactional
    public UserDto createUser(NewUserRequest newUserRequest) {
        User user = UserMapper.toUserEntity(newUserRequest);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
