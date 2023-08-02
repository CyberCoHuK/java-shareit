package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByOwnerId(long ownerId, Pageable pageable);

    Page<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String name, String description,
                                                                           Pageable pageable);

    List<Item> findAllByItemRequest(ItemRequest request);

    List<Item> findAllByItemRequestIn(List<ItemRequest> requests);
}
