package ru.practicum.shareit.booking.item.dto;

import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;

/**
 * // DTO для Item
 */
@Data
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long owner;
    private ItemRequest itemRequest;
}
