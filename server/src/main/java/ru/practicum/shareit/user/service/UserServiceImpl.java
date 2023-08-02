package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ObjectAlreadyExistException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Collection<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto, long userId) {
        User updateUser = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        Optional.ofNullable(userDto.getName()).ifPresent(updateUser::setName);
        Optional.ofNullable(userDto.getEmail()).ifPresent(updateUser::setEmail);
        User emailUser = userRepository.findUserByEmail(updateUser.getEmail());
        if (!Objects.equals(updateUser.getEmail(), emailUser.getEmail())) {
            throw new ObjectAlreadyExistException("Пользователь с такой почтой уже существует");
        }
        userRepository.save(updateUser);
        return UserMapper.toUserDto(updateUser);
    }

    @Transactional
    @Override
    public void deleteUserById(long userId) {
        userRepository.deleteById(userId);
    }
}
