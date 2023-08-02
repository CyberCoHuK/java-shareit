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
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

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

    Collection<Booking> findAllByItem(Item item, Sort sort);

    Optional<Booking> findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(long id, BookingStatus approved, LocalDateTime now);

    Optional<Booking> findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(long id, BookingStatus approved, LocalDateTime now);

    Optional<Booking> findFirstByItemIdAndStartBeforeOrderByStartDesc(long id, LocalDateTime now);

    Optional<Booking> findFirstByItemIdAndStartIsLessThanEqualOrderByStartDesc(long id, LocalDateTime now);

    Optional<Booking> findFirstByItemIdOrderByStartDesc(long id);

    Optional<Booking> findFirstByItemIdOrderByStartAsc(long id);

    Optional<Booking> findFirstBookingByItemIdAndStartBeforeOrderByStartDesc(long id, LocalDateTime now);

    Optional<Booking> findFirstBookingByItemIdAndStatusAndStartAfter(long id, BookingStatus approved, LocalDateTime now);

    Collection<Booking> findBookingByItemIdAndStartBeforeOrderByStartDesc(long id, LocalDateTime now);

    Collection<Booking> findBookingByItemIdAndStartBefore(long id, LocalDateTime now, Sort sort);

    Collection<Booking> findBookingByItemIdAndStartAfterAndStatus(long id, LocalDateTime now, BookingStatus approved, Sort sort);

    Collection<Booking> findBookingByItemId(long id, Sort sort);
}
