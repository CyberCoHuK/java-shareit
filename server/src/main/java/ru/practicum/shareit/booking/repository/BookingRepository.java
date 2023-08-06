package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByItemOwner(User booker, Pageable pageable);

    Page<Booking> findAllByItemOwnerAndStartBeforeAndEndAfter(User booker, LocalDateTime now, LocalDateTime now1,
                                                              Pageable pageable);

    Page<Booking> findAllByItemOwnerAndEndBefore(User booker, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByItemOwnerAndStartAfter(User booker, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByItemOwnerAndStatusEquals(User booker, BookingStatus waiting, Pageable pageable);

    Page<Booking> findAllByBookerId(Long booker, Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndBefore(Long booker, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long booker, LocalDateTime now, LocalDateTime now1,
                                                             Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartAfter(Long booker, LocalDateTime now, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatusEquals(Long booker, BookingStatus waiting, Pageable pageable);

    Collection<Booking> findAllByItemIdAndBookerIdAndStatusAndStartBefore(long itemId, long bookerId,
                                                                          BookingStatus bookingStatus,
                                                                          LocalDateTime now);

    List<Booking> findAllByItem(Item item, Sort sort);
}
