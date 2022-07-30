package ru.practicum.shareit.booking.item.model;

import lombok.Data;

/**
 * // Класс вещь
 */
@Data
public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private transient long userId;
}
