package ru.practicum.shareit.user;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    private int id;
    private String name;

    @NotNull(message = "Email равен null")
    @NotBlank(message = "Email пустой")
    @Email(regexp = "[a-z0-9A-Z._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "Ошибка в адресе почты")
    private String email;
}
