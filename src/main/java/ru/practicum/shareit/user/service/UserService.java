package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<UserDto> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public UserDto getUserById(long userId) {
        return userStorage.getUserById(userId);
    }

    public UserDto createUser(User user) {
        return userStorage.createUser(user);
    }

    public UserDto updateUser(User user, long userId) {
        return userStorage.updateUser(user, userId);
    }

    public void deleteUserById(long userId) {
        userStorage.deleteUserById(userId);
    }
}
