package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    @Transactional
    @Override
    public BookingCreateDto save(BookingCreateDto bookingCreateDto, Long userId) {
        validateForCreate(bookingCreateDto, userId);
        Booking booking = modelMapper.map(bookingCreateDto, Booking.class);
        booking.setBooker(userRepository.findById(userId).get());
        booking.setItem(itemRepository.findById(bookingCreateDto.getItemId()).get());
        bookingRepository.save(booking);
        return modelMapper.map(booking, BookingCreateDto.class);
    }

    @Transactional
    @Override
    public BookingDto update(Long userId, Long bookingId, Boolean approved) {
        validateForSetStatus(userId, bookingId, approved);
        Booking booking = bookingRepository.findById(bookingId).get();

        if (approved.equals(Boolean.TRUE)) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    public BookingDto get(Long userId, Long bookingId) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundException::new);
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
            return modelMapper.map(booking, BookingDto.class);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public List<BookingDto> getAllByCurrentUser(Long userId, State state) {
        checkUserById(userId);
        if (state == null) {
            state = State.ALL;
        }
        LocalDateTime now = LocalDateTime.now();
        List<Booking> resultBookings;
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
                resultBookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
                break;
            case CURRENT:
                resultBookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                    now, now);
                break;
            case FUTURE:
                resultBookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, now);
                break;
            default:
                throw new BadRequestException("Unknown state: " + state);
        }
        return getBookingsDto(resultBookings);
    }

    @Override
    public List<BookingDto> getAllByOwnedItems(Long userId, State state) {
        checkUserById(userId);
        if (state == null) {
            state = State.ALL;
        }
        List<Booking> resultBookings;
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                resultBookings = bookingRepository.findAllByOwnerIdOrderByEndDesc(userId);
                break;
            case WAITING:
                resultBookings =  bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId,
                    Status.WAITING);
                break;
            case REJECTED:
                resultBookings =  bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId,
                    Status.REJECTED);
                break;
            case PAST:
                resultBookings =bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, now);
                break;
            case CURRENT:
                resultBookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
                break;
            case FUTURE:
                resultBookings = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now);
                break;
            default:
                throw new BadRequestException("Unknown state: " + state);
        }
        return getBookingsDto(resultBookings);
    }

    private List<BookingDto> getBookingsDto(List<Booking> bookings) {
        return bookings.stream().map(booking -> modelMapper.map(booking,BookingDto.class)).collect(Collectors.toList());
    }

    private void validateForCreate(BookingCreateDto bookingCreateDto, Long userId) {
        checkUserById(userId);
        checkItemById(bookingCreateDto.getItemId());
        Optional<Item> optionalItem = itemRepository.findById(bookingCreateDto.getItemId());
        if (optionalItem.get().getAvailable().equals(Boolean.FALSE)) {
            throw new BadRequestException("Этот предмет не доступен для аренды");
        }

        if (bookingCreateDto.getEnd().isBefore(bookingCreateDto.getStart())
            || bookingCreateDto.getStart().isBefore(LocalDateTime.now())
            || bookingCreateDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Продолжительность аренды не верно указано");
        }

        Optional<Item> item = itemRepository.findById(bookingCreateDto.getItemId());
        if (item.get().getOwner().getId().equals(userId)) {
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
        Long id = bookingRepository
            .findById(bookingId)
            .get()
            .getItem().getId();
        Long ownerId = itemRepository
            .findById(id)
            .get()
            .getOwner().getId();

        if (!ownerId.equals(userId)) {
            throw new NotFoundException("Пользователь id = " + userId + " не является владельцем предмета");
        }
    }

    private void checkUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
    }

    private void checkItemById(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new NotFoundException("Предмет с id = " + itemId + " не найден");
        }
    }

    private void checkBookingById(Long bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new NotFoundException("Бронирование id = " + bookingId + " не найден");
        }
    }
}
