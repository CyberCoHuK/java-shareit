package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.enums.BookingStates;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.enums.BookingStatus.*;
import static ru.practicum.shareit.enums.Sorts.START;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final Sort sort = Sort.by(START.getSort()).descending();

    @Transactional
    @Override
    public BookingDto createBooking(BookingDtoShort bookingDtoShort, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        Item item = itemRepository.findById(bookingDtoShort.getItemId())
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Вещи с id = " + bookingDtoShort.getItemId() + " не существует"));
        if (item.getOwner().getId() == userId) {
            throw new ObjectNotFoundException("Пользователь не может забронировать вещь принадлежащую ему");
        }
        if (!item.getAvailable()) {
            throw new BadRequestException("Вещь не доступна. Невозможно создать бронирование");
        }
        if (bookingDtoShort.getEnd().isBefore(bookingDtoShort.getStart()) ||
                bookingDtoShort.getEnd().isEqual(bookingDtoShort.getStart())) {
            throw new BadRequestException(
                    "Дата окончания не может быть раньше или равна дате начала бронирования." +
                            " Невозможно создать бронирование");
        }
        Booking booking = BookingMapper.toBooking(bookingDtoShort, user, item);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto requestStatusDecision(Long userId, Long bookingId, Boolean approved) {
        userRepository.isExist(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException("Бронирования с id = " + bookingId + " не существует"));
        if (!booking.getStatus().equals(WAITING)) {
            throw new BadRequestException("Невозможно забронировать вещь со статусом: " + booking.getStatus());
        }
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ObjectNotFoundException("Не подходящий индентификатор пользователя: " + userId);
        }
        booking.setStatus(approved ? APPROVED : REJECTED);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDto getBookingByUser(Long userId, Long bookingId) {
        userRepository.isExist(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException("Бронирования с id = " + bookingId + " не существует"));
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new ObjectNotFoundException("Не подходящий индентификатор пользователя: " + userId);
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<BookingDto> getAllBookingsByUser(Long userId, BookingStates state) {
        userRepository.isExist(userId);
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings.addAll(bookingRepository.findAllByBookerId(userId, sort));
                break;
            case CURRENT:
                bookings.addAll(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(
                        userId, LocalDateTime.now(), LocalDateTime.now(), sort));
                break;
            case PAST:
                bookings.addAll(bookingRepository.findAllByBookerIdAndEndBefore(userId, LocalDateTime.now(), sort));
                break;
            case FUTURE:
                bookings.addAll(bookingRepository.findAllByBookerIdAndStartAfter(userId, LocalDateTime.now(), sort));
                break;
            case WAITING:
                bookings.addAll(bookingRepository.findAllByBookerIdAndStatusEquals(userId, WAITING, sort));
                break;
            case REJECTED:
                bookings.addAll(bookingRepository.findAllByBookerIdAndStatusEquals(userId, REJECTED, sort));
                break;
            default:
                throw new BadRequestException("Unknown state: " + state);
        }
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<BookingDto> getAllBookingsByOwner(Long ownerId, BookingStates state) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + ownerId + " не существует"));
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings.addAll(bookingRepository.findAllByItemOwner(owner, sort));
                break;
            case CURRENT:
                bookings.addAll(bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(
                        owner, LocalDateTime.now(), LocalDateTime.now(), sort));
                break;
            case PAST:
                bookings.addAll(bookingRepository.findAllByItemOwnerAndEndBefore(owner, LocalDateTime.now(), sort));
                break;
            case FUTURE:
                bookings.addAll(bookingRepository.findAllByItemOwnerAndStartAfter(owner, LocalDateTime.now(), sort));
                break;
            case WAITING:
                bookings.addAll(bookingRepository.findAllByItemOwnerAndStatusEquals(owner, WAITING, sort));
                break;
            case REJECTED:
                bookings.addAll(bookingRepository.findAllByItemOwnerAndStatusEquals(owner, REJECTED, sort));
                break;
            default:
                throw new BadRequestException("Unknown state: " + state);
        }
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}
