package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

@Service
public class UserClient extends BaseClient {

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/users"))
                        .build()
        );
    }

    public Object saveUser(UserDto user) {
        return post("", 0, user);
    }

    public Object updateUser(UserDto userDto, int userId) {
        return patch("/" + userId, userId, userDto);
    }

    public Object getUser(int userId) {
        return get("/" + userId, userId);
    }

    public Object getAllUsers() {
        return get("", 0);
    }

    public Object deleteUser(int userId) {
        return delete("/" + userId, userId);
    }
}
