package ru.practicum.shareit.user;


import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    /**
     * Создание нового user
     * @param user
     * @return
     */
    UserDto saveUser(UserDto userDto);
    User updateUser(long id, User user);
    User getUser(long userId);
    void deleteUser(long userId);
    boolean checkUser(long userId);
}
