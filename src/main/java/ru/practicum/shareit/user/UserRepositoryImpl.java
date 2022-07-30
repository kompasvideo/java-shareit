package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.InternalServerError;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Component
class UserRepositoryImpl implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private long delete = 0;

    @Override
    public List<User> findAll() {
        return users;
    }

    /**
     * Создание нового user
     * @param user
     * @return
     */
    @Override
    public User save(User user) {
        valid(user);
        user.setId(getId());
        users.add(user);
        return user;
    }

    @Override
    public User update(long id, User user) {
        for (User fUser: users ) {
            if (fUser.getId() == id){
                if (user.getEmail() != null) {
                    valid(user);
                    fUser.setEmail(user.getEmail());
                }
                if (user.getName() != null) {
                    fUser.setName(user.getName());
                }
                user = fUser;
                break;
            }
        }
        return user;
    }

    @Override
    public User get(long userId) {
        for (User fUser: users ) {
            if (fUser.getId() == userId){
                return fUser;
            }
        }
        return new User();
    }

    @Override
    public void delete(long userId) {
        User user = null;
        for (User fUser: users ) {
            if (fUser.getId() == userId){
                user = fUser;
            }
        }
        if ( user != null) {
            users.remove(user);
            delete++;
        }
    }

    @Override
    public boolean checkUser(long userId){
        for (User fUser: users ) {
            if (fUser.getId() == userId){
                if (! fUser.getEmail().equals("")){
                    return true;
                }
            }
        }
        return false;
    }

    private long getId() {
        long lastId = users.stream()
            .mapToLong(User::getId)
            .max()
            .orElse(0);
        return lastId + 1 + delete;
    }

    private void valid(User user) {
        for (User fUser: users ) {
            if (fUser.getEmail().equals(user.getEmail())){
                throw new InternalServerError("Такой email уже зарегистрирован");
            }
            if (user.getEmail() == null ){
                throw new ValidationException("Email не указан");
            }
            if (user.getEmail().isEmpty()){
                throw new ValidationException("Email не указан");
            }
            if(! user.getEmail().contains("@")){
                throw new ValidationException("Ошибка в Email");
            }
        }
    }
}
