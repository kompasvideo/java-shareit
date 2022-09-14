package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import javax.validation.Valid;

/**
 * // Контроллёр для User
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserGatewayController {
    private UserClient userClient;

    @PostMapping
    public Object saveNewUser(@Valid @RequestBody UserDto userDto) {
        log.info("gateway - Add new user");
        return userClient.saveUser(userDto);
    }

    @PatchMapping("/{id}")
    public Object updateUser(@RequestBody UserDto userDto, @PathVariable long id) {
        log.info("gateway - Update user");
        return userClient.updateUser(userDto, id);
    }

    @GetMapping("/{id}")
    public Object getUser(@PathVariable long id) {
        log.info("gateway - Get user id: {}", id);
        return userClient.getUser(id);
    }

    @GetMapping
    public Object getAllUsers() {
        log.info("gateway - Get all users");
        return userClient.getAll();
    }

    @DeleteMapping("/{id}")
    public Object deleteUser(@PathVariable long id) {
        log.info("gateway - Delete user id: {}", id);
        return userClient.deleteUser(id);
    }

}
