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
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream()
            .map(userMapper::mapToUserDto)
            .collect(Collectors.toList());
    }

    /**
     * Создание нового user
     *
     * @param userDto
     * @return
     */
    @Override
    public UserDto saveUser(UserDto userDto) {
        return userMapper.mapToUserDto(repository.save(userMapper.mapToUser(userDto)));
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        return userMapper.mapToUserDto(repository.update(id, userMapper.mapToUser(userDto)));
    }

    @Override
    public UserDto getUser(long userId) {
        return userMapper.mapToUserDto(repository.get(userId));
    }

    @Override
    public void deleteUser(long userId) {
        repository.delete(userId);
    }

    @Override
    public boolean checkUser(long userId) {
        return repository.checkUser(userId);
    }

}
