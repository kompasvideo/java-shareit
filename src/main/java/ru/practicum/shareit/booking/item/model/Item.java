package ru.practicum.shareit.booking.item.model;

import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;

/**
 * // Класс вещь
 */
@Data
public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private transient long owner;
    private ItemRequest itemRequest;
}
