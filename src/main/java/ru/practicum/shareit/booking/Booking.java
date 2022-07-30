package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.booking.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * // класс бронирования
 */
@Data
public class Booking {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private UserDto booker;
    private Status status;
}
