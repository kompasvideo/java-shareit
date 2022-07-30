package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.item.MappingItem;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final MappingUser mappingUser;

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    /**
     * Создание нового user
     * @param userDto
     * @return
     */
    @Override
    public UserDto saveUser(UserDto userDto) {
        return mappingUser.mapToUserDto(repository.save(mappingUser.mapToUser(userDto)));
    }

    @Override
    public  User updateUser(long id, User user){ return repository.update(id, user);}

    @Override
    public User getUser(long userId){ return repository.get(userId);}

    @Override
    public void deleteUser(long userId){ repository.delete(userId);}

    @Override
    public boolean checkUser(long userId){
        return repository.checkUser(userId);
    }

}
