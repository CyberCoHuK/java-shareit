package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserStorage extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    default void isExist(long userId){
        findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
    }
}
