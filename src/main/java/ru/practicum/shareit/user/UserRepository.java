package ru.practicum.shareit.user;


import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

interface UserRepository {
    List<User> findAll();

    /**
     * Создание нового user
     * @param user
     * @return
     */
    User save(User user);
    User update(long id, User user);
    User get(long userId);
    void delete(long userId);
    boolean checkUser(long userId);
}