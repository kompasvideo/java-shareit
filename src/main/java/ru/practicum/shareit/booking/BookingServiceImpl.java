package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
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
        Optional<User> optionalUser = userRepository.findById(userId);
        booking.setBooker(optionalUser.orElseThrow());
        Optional<Item> optionalItem = itemRepository.findById(bookingCreateDto.getItemId());
        booking.setItem(optionalItem.orElseThrow());
        bookingRepository.save(booking);
        return modelMapper.map(booking, BookingCreateDto.class);
    }

    @Transactional
    @Override
    public BookingDto update(Long userId, Long bookingId, Boolean approved) {
        validateForSetStatus(userId, bookingId, approved);
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        Booking booking = optionalBooking.orElseThrow();

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
    public List<BookingDto> getAllByCurrentUser(Long userId, String stringState, int from, int size) {
        checkUserById(userId);
        State state;
        if (stringState == null) {
            state = State.ALL;
        } else if (stringState.isEmpty()) {
            state = State.ALL;
        } else state = State.valueOf(stringState);
        LocalDateTime now = LocalDateTime.now();
        Page<Booking> resultBookings;
        if (from == size) {
            from--;
        }
        PageRequest pageRequest = PageRequest.of(from, size);
        switch (state) {
            case ALL:
                resultBookings = bookingRepository.findAllByBookerIdOrderByEndDesc(userId, pageRequest);
                break;
            case WAITING:
                resultBookings = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(userId, Status.WAITING,
                    pageRequest);
                break;
            case REJECTED:
                resultBookings = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(userId, Status.REJECTED,
                    pageRequest);
                break;
            case PAST:
                resultBookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, now,
                    pageRequest);
                break;
            case CURRENT:
                resultBookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                    now, now, pageRequest);
                break;
            case FUTURE:
                resultBookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, now,
                    pageRequest);
                break;
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
        return resultBookings.stream()
            .map(booking -> modelMapper.map(booking, BookingDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllByOwnedItems(Long userId, String stringState, int from, int size) {
        checkUserById(userId);
        State state;
        if (stringState == null) {
            state = State.ALL;
        } else if (stringState.isEmpty()) {
            state = State.ALL;
        } else state = State.valueOf(stringState);
        Page<Booking> resultBookings;
        PageRequest pageRequest = PageRequest.of(from, size);
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                resultBookings = bookingRepository.findAllByOwnerIdOrderByEndDesc(userId, pageRequest);
                break;
            case WAITING:
                resultBookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId,
                    Status.WAITING, pageRequest);
                break;
            case REJECTED:
                resultBookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId,
                    Status.REJECTED, pageRequest);
                break;
            case PAST:
                resultBookings = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, now,
                    pageRequest);
                break;
            case CURRENT:
                resultBookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                    now, now, pageRequest);
                break;
            case FUTURE:
                resultBookings = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now,
                    pageRequest);
                break;
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
        return resultBookings.stream()
            .map(booking -> modelMapper.map(booking, BookingDto.class)).collect(Collectors.toList());
    }

    private List<BookingDto> getBookingsDto(List<Booking> bookings) {
        return bookings.stream().map(booking -> modelMapper.map(booking, BookingDto.class)).collect(Collectors.toList());
    }

    private void validateForCreate(BookingCreateDto bookingCreateDto, Long userId) {
        checkUserById(userId);
        checkItemById(bookingCreateDto.getItemId());
        Optional<Item> optionalItem = itemRepository.findById(bookingCreateDto.getItemId());
        if (Boolean.FALSE.equals(optionalItem.orElseThrow().getAvailable())) {
            throw new BadRequestException();
        }

        if (bookingCreateDto.getEnd().isBefore(bookingCreateDto.getStart())
            || bookingCreateDto.getStart().isBefore(LocalDateTime.now())
            || bookingCreateDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new BadRequestException();
        }

        Optional<Item> item = itemRepository.findById(bookingCreateDto.getItemId());
        if (item.orElseThrow().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь id = "
                + userId + " является владельцем предмета id = " + bookingCreateDto.getItemId());
        }
    }

    private void validateForSetStatus(Long userId, Long bookingId, Boolean approved) {
        checkUserById(userId);
        checkBookingById(bookingId);
        if (approved == null) {
            throw new BadRequestException();
        }

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (Status.APPROVED.equals(optionalBooking.orElseThrow().getStatus())
            && approved.equals(Boolean.TRUE)) {
            throw new BadRequestException();
        }
        Long id = optionalBooking
            .orElseThrow()
            .getItem().getId();
        Optional<Item> optionalItem = itemRepository.findById(id);
        Long ownerId = optionalItem
            .orElseThrow()
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
