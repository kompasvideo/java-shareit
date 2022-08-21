package ru.practicum.shareit.requests;

import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * // Класс запрос
 */
@Data
public class ItemRequest {
    private long id;
    private String description;
    private UserDto requestor;
    private LocalDateTime creater;

}
