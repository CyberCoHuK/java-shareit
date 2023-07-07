package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage extends JpaRepository<Item, Long> {
    Collection<Item> findAllByOwner(long userId);

    Collection<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String name, String description);

}
