package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

/**
 * // TODO .
 */
@Data
@AllArgsConstructor
public class ItemRequestDto {
    private static User requestor;
    private String description;
    private LocalDate created;
}
