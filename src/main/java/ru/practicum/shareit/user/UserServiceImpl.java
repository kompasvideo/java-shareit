package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final MappingUser mappingUser;

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream()
            .map(mappingUser::mapToUserDto)
            .collect(Collectors.toList());
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
    public  UserDto updateUser(long id, UserDto userDto){
        return mappingUser.mapToUserDto(repository.update(id, mappingUser.mapToUser(userDto)));
    }

    @Override
    public UserDto getUser(long userId){
        return mappingUser.mapToUserDto(repository.get(userId));
    }

    @Override
    public void deleteUser(long userId){ repository.delete(userId);}

    @Override
    public boolean checkUser(long userId){
        return repository.checkUser(userId);
    }

}
