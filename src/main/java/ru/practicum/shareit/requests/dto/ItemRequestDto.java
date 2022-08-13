package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * // TODO .
 */
@Data
@AllArgsConstructor
public class ItemRequestDto {
    private String description;
    private static User requestor;
    private LocalDate created;
}
