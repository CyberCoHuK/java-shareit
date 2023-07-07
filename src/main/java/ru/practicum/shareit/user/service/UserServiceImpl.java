package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectAlreadyExistException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public Collection<UserDto> getAllUsers() {
        return userStorage.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(long userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        return UserMapper.toUserDto(user);
    }

    public UserDto createUser(UserDto userDto) {
        User user = userStorage.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    public UserDto updateUser(UserDto userDto, long userId) {
        User updateUser = userStorage.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        Optional.ofNullable(userDto.getName()).ifPresent(updateUser::setName);
        Optional.ofNullable(userDto.getEmail()).ifPresent(updateUser::setEmail);
        Optional<User> emailUser = userStorage.findUserByEmail(updateUser.getEmail());
        if (!Objects.equals(updateUser.getEmail(), emailUser.get().getEmail())) {
            throw new ObjectAlreadyExistException("Пользователь с такой почтой уже существует");
        }
        userStorage.save(updateUser);
        return UserMapper.toUserDto(updateUser);
    }

    public void deleteUserById(long userId) {
        userStorage.deleteById(userId);
    }
}
