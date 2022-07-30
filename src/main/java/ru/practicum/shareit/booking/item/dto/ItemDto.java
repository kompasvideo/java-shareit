package ru.practicum.shareit.booking.item.dto;

import lombok.Data;

/**
 * // DTO для Item
 */
@Data
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private transient long userId;
}
