package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findAllByItemOwner(User booker, Sort sort);

    Collection<Booking> findAllByItemOwnerAndStartBeforeAndEndAfter(User booker, LocalDateTime now, LocalDateTime now1,
                                                                    Sort sort);

    Collection<Booking> findAllByItemOwnerAndEndBefore(User booker, LocalDateTime now, Sort sort);

    Collection<Booking> findAllByItemOwnerAndStartAfter(User booker, LocalDateTime now, Sort sort);

    Collection<Booking> findAllByItemOwnerAndStatusEquals(User booker, BookingStatus waiting, Sort sort);

    Collection<Booking> findAllByBookerId(Long booker, Sort sort);

    Collection<Booking> findAllByBookerIdAndEndBefore(Long booker, LocalDateTime now, Sort sort);

    Collection<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Long booker, LocalDateTime now, LocalDateTime now1,
                                                                   Sort sort);

    Collection<Booking> findAllByBookerIdAndStartAfter(Long booker, LocalDateTime now, Sort sort);

    Collection<Booking> findAllByBookerIdAndStatusEquals(Long booker, BookingStatus waiting, Sort sort);

    Collection<Booking> findAllByItemIdAndBookerIdAndStatusAndStartBefore(long itemId, long bookerId, BookingStatus bookingStatus,
                                                                          LocalDateTime now);

    Collection<Booking> findAllByItem(Item item, Sort sort);
}
