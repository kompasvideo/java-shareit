package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        validate(user);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUser(long userId, User updatedUser) {
        validateForUpdateUser(userId, updatedUser);
        User user = userRepository.findById(userId).get();
        if (updatedUser.getName() != null)
            user.setName(updatedUser.getName());
        if (updatedUser.getEmail() != null)
            user.setEmail(updatedUser.getEmail());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUser(long userId) {
        userIdValidate(userId);
        return userRepository.findById(userId).get();
    }

    @Transactional
    @Override
    public void deleteUser(long userId) {
        userIdValidate(userId);
        userRepository.deleteById(userId);
    }


    private void validate(@Valid User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new BadRequestException("email не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            throw new BadRequestException("email не имеет @");
        }
        if (user.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым");
        }
    }

    private void validateForUpdateUser(Long userId, User user) {
        userIdValidate(userId);
        if (user.getEmail() != null) {
            emailValidate(user.getEmail());
        }
    }

    private void emailValidate(String email) {
        if (userRepository.findAll().stream().anyMatch(user -> user.getEmail().equals(email))) {
            throw new ValidationException("email уже существует");
        }
    }

    private void userIdValidate(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Юзер не найден, id = " + userId);
        }
    }
}

