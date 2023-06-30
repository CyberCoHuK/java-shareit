package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<UserDto> getAllUsers();

    UserDto getUserById(long userId);

    UserDto createUser(User user);

    UserDto updateUser(User user, long userId);

    void deleteUserById(long userId);

    void isExist(long userId);
}
