package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ObjectAlreadyExistException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserInMemoryStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> mails = new HashSet<>();
    private long userId = 1L;

    @Override
    public Collection<UserDto> getAllUsers() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users.values()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long userId) {
        isExist(userId);
        log.info("Пользователь с индентификатором {} возвращен", users.get(userId));
        return UserMapper.toUserDto(users.get(userId));
    }

    @Override
    public UserDto createUser(User user) {
        mailCheck(user.getEmail());
        user.setId(userId++);
        mails.add(user.getEmail());
        users.put(user.getId(), user);
        log.info("Добавлен пользователь с индентификатором {}", user.getId());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(User user, long userId) {
        isExist(userId);
        user.setId(userId);
        if (user.getEmail() != null) {
            if (!Objects.equals(user.getEmail(), users.get(userId).getEmail())) {
                mailCheck(user.getEmail());
            }
            mails.remove(users.get(userId).getEmail());
            mails.add(user.getEmail());
        } else {
            user.setEmail(users.get(userId).getEmail());
        }
        if (user.getName() == null) {
            user.setName(users.get(userId).getName());
        }
        users.replace(userId, user);
        log.info("Обновлен пользователь с индентификатором {}", userId);
        return UserMapper.toUserDto(users.get(userId));
    }

    @Override
    public void deleteUserById(long userId) {
        isExist(userId);
        mails.remove(users.get(userId).getEmail());
        users.remove(userId);
        log.info("Пользователь с индентификатором {} удален", userId);
    }

    public void isExist(long userId) {
        if (!users.containsKey(userId)) {
            throw new ObjectNotFoundException("Пользователя с таким индентификатором " + userId + " не существует");
        }
    }

    private void mailCheck(String email) {
        if (mails.contains(email)) {
            throw new ObjectAlreadyExistException("Пользователь с такой почтой уже существует");
        }
    }
}
