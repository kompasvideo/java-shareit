package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.item.ItemRepository;
import ru.practicum.shareit.booking.item.model.Item;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingCreateDto save(BookingCreateDto bookingCreateDto, Long userId) {
        validateForCreate(bookingCreateDto, userId);
        Booking booking = BookingMapper.toBooking(bookingCreateDto, userId);
        bookingRepository.save(booking);
        return BookingMapper.toBookingCreateDto(booking);
    }

    @Transactional
    @Override
    public BookingDto update(Long userId, Long bookingId, Boolean approved) {
        validateForSetStatus(userId, bookingId, approved);
        Booking booking = bookingRepository.findById(bookingId).get();

        if (approved.equals(Boolean.TRUE)) {
            booking.setStatus(Status.APPROVED);
            bookingRepository.save(booking);
        } else {
            booking.setStatus(Status.REJECTED);
            bookingRepository.save(booking);
        }
        return BookingMapper.toBookingDto(booking,
            new BookingDto.Item(booking.getItemId(),
                itemRepository.findById(booking.getItemId()).get().getName()),
            new BookingDto.Book(booking.getBookerId()));
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDto get(Long userId, Long bookingId) {
        checkBookerAndOwner(userId, bookingId);
        Booking booking = bookingRepository.findById(bookingId).get();
        return BookingMapper.toBookingDto(booking,
            new BookingDto.Item(booking.getItemId(),
                itemRepository.findById(booking.getItemId()).get().getName()),
            new BookingDto.Book(booking.getBookerId()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getAllByCurrentUser(Long userId, State state) {
        checkUserById(userId);
        if (state == null) {
            state = State.ALL;
        }
        List<Booking> resultBookings = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByEndDesc(userId);
        switch (state) {
            case ALL:
                resultBookings = bookingRepository.findAllByBookerIdOrderByEndDesc(userId);
                break;
            case WAITING:
                resultBookings = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                resultBookings = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(userId, Status.REJECTED);
                break;
            case PAST:
                for (Booking booking : bookings) {
                    if (booking.getEnd().isBefore(LocalDateTime.now())) {
                        resultBookings.add(booking);
                    }
                }
                break;
            case CURRENT:
                for (Booking booking : bookings) {
                    if (booking.getStart().isBefore(LocalDateTime.now())
                        && booking.getEnd().isAfter(LocalDateTime.now())) {
                        resultBookings.add(booking);
                    }
                }
                break;
            case FUTURE:
                for (Booking booking : bookings) {
                    if (booking.getEnd().isAfter(LocalDateTime.now())) {
                        resultBookings.add(booking);
                    }
                }
                break;
            default:
                throw new BadRequestException("Unknown state: " + state);
        }
        return getBookingsDto(resultBookings);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getAllByOwnedItems(Long userId, State state) {
        checkUserById(userId);
        if (state == null) {
            state = State.ALL;
        }
        List<Booking> resultBookings = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findAllByOwnerIdOrderByEndDesc(userId);
        switch (state) {
            case ALL:
                resultBookings = bookingRepository.findAllByOwnerIdOrderByEndDesc(userId);
                break;
            case WAITING:
                resultBookings = bookingRepository.findAllByOwnerIdAndStatusOrderByEndDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                resultBookings = bookingRepository.findAllByOwnerIdAndStatusOrderByEndDesc(userId, Status.REJECTED);
                break;
            case PAST:
                for (Booking booking : bookings) {
                    if (booking.getEnd().isBefore(LocalDateTime.now())) {
                        resultBookings.add(booking);
                    }
                }
                break;
            case CURRENT:
                for (Booking booking : bookings) {
                    if (booking.getStart().isBefore(LocalDateTime.now())
                        && booking.getEnd().isAfter(LocalDateTime.now())) {
                        resultBookings.add(booking);
                    }
                }
                break;
            case FUTURE:
                for (Booking booking : bookings) {
                    if (booking.getEnd().isAfter(LocalDateTime.now())) {
                        resultBookings.add(booking);
                    }
                }
                break;
            default:
                throw new BadRequestException("Unknown state: " + state);
        }
        return getBookingsDto(resultBookings);
    }

    private List<BookingDto> getBookingsDto(List<Booking> bookings) {
        List<BookingDto> bookingsDto = new ArrayList<>();
        bookings.forEach(booking -> bookingsDto.add(BookingMapper.toBookingDto(booking,
            new BookingDto.Item(booking.getItemId(), itemRepository.findById(booking.getItemId()).get().getName()),
            new BookingDto.Book(booking.getBookerId()))));
        return bookingsDto;
    }

    private void validateForCreate(BookingCreateDto bookingCreateDto, Long userId) {
        checkUserById(userId);
        checkItemById(bookingCreateDto.getItemId());

        if (itemRepository.findById(bookingCreateDto.getItemId()).get().getAvailable().equals(Boolean.FALSE)) {
            throw new BadRequestException("Этот предмет не доступен для аренды");
        }

        if (bookingCreateDto.getEnd().isBefore(bookingCreateDto.getStart())
            || bookingCreateDto.getStart().isBefore(LocalDateTime.now())
            || bookingCreateDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Продолжительность аренды не верно указано");
        }

        Optional<Item> item = itemRepository.findById(bookingCreateDto.getItemId());
        if (item.get().getOwnerId().equals(userId)) {
            throw new NotFoundException("Пользователь id = "
                + userId + " является владельцем предмета id = " + bookingCreateDto.getItemId());
        }
    }

    private void validateForSetStatus(Long userId, Long bookingId, Boolean approved) {
        checkUserById(userId);
        checkBookingById(bookingId);
        if (approved == null) {
            throw new BadRequestException("Параметр approved не был передан");
        }

        if (bookingRepository.findById(bookingId).get().getStatus().equals(Status.APPROVED)
            && approved.equals(Boolean.TRUE)) {
            throw new BadRequestException("Повторное подтверждение бронирование не допустимо");
        }
        Long ownerId = itemRepository
            .findById(bookingRepository
                .findById(bookingId)
                .get()
                .getItemId())
            .get()
            .getOwnerId();

        if (!ownerId.equals(userId)) {
            throw new NotFoundException("Пользователь id = " + userId + " не является владельцем предмета");
        }
    }

    private void checkBookerAndOwner(Long userId, Long bookingId) {
        checkUserById(userId);
        checkBookingById(bookingId);
        Long bookerId = bookingRepository
            .findById(bookingId)
            .get()
            .getBookerId();
        Long ownerId = itemRepository
            .findById(bookingRepository
                .findById(bookingId)
                .get().getItemId())
            .get().getOwnerId();
        if (!bookerId.equals(userId) && !ownerId.equals(userId)) {
            throw new NotFoundException("Пользователь id = " + userId + " не является владельцем предмета или автором бронирования");
        }
    }

    private void checkUserById(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
    }

    private void checkItemById(Long itemId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new NotFoundException("Предмет с id = " + itemId + " не найден");
        }
    }

    private void checkBookingById(Long bookingId) {
        if (bookingRepository.findById(bookingId).isEmpty()) {
            throw new NotFoundException("Бронирование id = " + bookingId + " не найден");
        }
    }
}
