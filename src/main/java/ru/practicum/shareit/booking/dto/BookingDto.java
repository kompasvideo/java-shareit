package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private Status status;
}