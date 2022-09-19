package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.InternalServerError;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

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
        if (userRepository.findByEmail(user.getEmail()) != null) {
            userRepository.save(user);
            throw new InternalServerError();
        }
        log.info(user.getId().toString());
        log.info(user.getName());
        log.info(user.getEmail());
        return userRepository.save(user);
    }


    @Transactional
    @Override
    public User updateUser(long userId, User updatedUser) {
        validateForUpdateUser(userId, updatedUser);
        User user = userRepository.findById(userId).orElseThrow();
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
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElseThrow();
    }

    @Transactional
    @Override
    public void deleteUser(long userId) {
        userIdValidate(userId);
        userRepository.deleteById(userId);
    }

    private void validateForUpdateUser(Long userId, User user) {
        userIdValidate(userId);
        if (user.getEmail() != null) {
            emailValidate(user.getEmail());
        }
    }

    private void emailValidate(String email) {
        List<User> users = userRepository.findAll();
        if (users.stream().anyMatch(user -> user.getEmail().equals(email))) {
            throw new InternalServerError();
        }
    }

    private void userIdValidate(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Юзер не найден, id = " + userId);
        }
    }
}

