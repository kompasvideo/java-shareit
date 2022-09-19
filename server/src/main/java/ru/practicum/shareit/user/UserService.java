package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    /**
     * Создание нового user
     *
     * @param userDto
     * @return
     */
    User saveUser(User userDto);

    User updateUser(long userId, User updatedUser);

    User getUser(long userId);

    void deleteUser(long userId);

}


