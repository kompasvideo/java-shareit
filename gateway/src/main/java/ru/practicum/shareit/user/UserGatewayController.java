package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserGatewayController {

    private UserClient userClient;

    @PostMapping
    public Object saveNewUser(@Valid @RequestBody UserDto userDto) {
        log.info("UserGatewayController saveNewUser");
        return userClient.saveUser(userDto);
    }

    @PatchMapping("/{userId}")
    public Object updateUser(@RequestBody UserDto userDto, @PathVariable int userId) {
        log.info("UserGatewayController updateUser");
        return userClient.updateUser(userDto, userId);
    }

    @GetMapping("/{userId}")
    public Object getUser(@PathVariable int userId) {
        log.info("UserGatewayController getUser userId: {}", userId);
        return userClient.getUser(userId);
    }

    @GetMapping
    public Object getAllUsers() {
        log.info("UserGatewayController getAllUsers");
        return userClient.getAllUsers();
    }

    @DeleteMapping("/{userId}")
    public Object deleteUser(@PathVariable int userId) {
        log.info("UserGatewayController deleteUser userId: {}", userId);
        return userClient.deleteUser(userId);
    }
}
