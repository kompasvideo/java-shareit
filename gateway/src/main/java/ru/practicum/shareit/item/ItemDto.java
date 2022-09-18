package ru.practicum.shareit.item;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ItemDto {

    private Integer id;

    @NotEmpty(message = "Name пустое")
    @NotNull(message = "Name равно null")
    private String name;

    @NotEmpty(message = "Description пустое")
    @NotNull(message = "Description равно null")
    private String description;

    @NotNull(message = "available равно null")
    private Boolean available;

    private Integer requestId;
}
