package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.item.model.Item;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingCreateDto {
    private Long id;
    private Long itemId;
    //private Item item;
    private LocalDateTime start;
    private LocalDateTime end;
}