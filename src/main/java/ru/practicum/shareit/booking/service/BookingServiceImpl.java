package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.Collection;
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
        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto requestStatusDecision(Long userId, Long bookingId, Boolean approved) {
        userRepository.isExist(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException("Бронирования с id = " + bookingId + " не существует"));
        if (booking.getStatus() != WAITING) {
            throw new BadRequestException("Невозможно забронировать вещь со статусом: " + booking.getStatus());
        }
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ObjectNotFoundException("Не подходящий индентификатор пользователя: " + userId);
        }
        booking.setStatus(approved ? APPROVED : REJECTED);
        booking = bookingRepository.save(booking);
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
    public Collection<BookingDto> getAllBookingsByUser(Long userId, BookingStates state, Integer from, Integer size) {
        userRepository.isExist(userId);
        Page<Booking> bookings;
        PageRequest page = PageRequest.of(from / size, size, sort);
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerId(userId, page);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(
                        userId, LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBefore(userId, LocalDateTime.now(), page);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfter(userId, LocalDateTime.now(), page);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatusEquals(userId, WAITING, page);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatusEquals(userId, REJECTED, page);
                break;
            default:
                throw new BadRequestException("Unknown state: " + state);
        }
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<BookingDto> getAllBookingsByOwner(Long ownerId, BookingStates state, Integer from, Integer size) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + ownerId + " не существует"));
        Page<Booking> bookings;
        PageRequest page = PageRequest.of(from / size, size, sort);
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemOwner(owner, page);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(
                        owner, LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            case PAST:
                bookings = bookingRepository.findAllByItemOwnerAndEndBefore(owner, LocalDateTime.now(), page);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItemOwnerAndStartAfter(owner, LocalDateTime.now(), page);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItemOwnerAndStatusEquals(owner, WAITING, page);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItemOwnerAndStatusEquals(owner, REJECTED, page);
                break;
            default:
                throw new BadRequestException("Unknown state: " + state);
        }
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}
