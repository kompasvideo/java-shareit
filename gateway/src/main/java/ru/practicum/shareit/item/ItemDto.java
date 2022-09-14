package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * // DTO для Item
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private long id;

    @NotEmpty(message = "Name can't be empty")
    @NotNull(message = "Name can't be null")
    private String name;

    @NotEmpty(message = "Description can't be empty")
    @NotNull(message = "Description can't be null")
    private String description;

    @NotNull(message = "Available can't be null")
    private Boolean available;

    //@Nullable
    private long requestId;
}
