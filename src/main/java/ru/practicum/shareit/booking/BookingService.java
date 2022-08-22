package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto get(Long userId, Long bookingId);
    BookingCreateDto save(BookingCreateDto bookingCreateDto, Long userId);

    BookingDto update(Long userId, Long bookingId, Boolean approved);


    List<BookingDto> getAllByCurrentUser(Long userId, State state);

    List<BookingDto> getAllByOwnedItems(Long userId, State state);
}

