package ru.practicum.shareit.user;


import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    /**
     * Создание нового user
     * @param userDto
     * @return
     */
    UserDto saveUser(UserDto userDto);
    UserDto updateUser(long id, UserDto userDto);
    UserDto getUser(long userId);
    void deleteUser(long userId);
    boolean checkUser(long userId);
}
