package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    /**
     * Создание нового user
     *
     * @param userDto
     * @return
     */
    User saveUser(User userDto) throws Throwable;

    User updateUser(long userId, User updatedUser) throws Throwable;

    User getUser(long userId);

    void deleteUser(long userId);

}


