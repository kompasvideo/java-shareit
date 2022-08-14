package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * // Контроллёр для User
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createNewUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable("id") Long userId, @RequestBody User updatedUser) {
        return userService.update(userId, updatedUser);
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable("id") Long userId) {
        return userService.findUserById(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable("id") Long userId) {
        userService.deleteUserById(userId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}


