package ru.practicum.shareit.booking.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * // DTO для Item
 */
@Data
@Builder
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
}
