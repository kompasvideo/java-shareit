package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
            builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .build()
        );
    }

    public Object saveUser(UserDto user) {
        return post("", 0, user);
    }

    public Object updateUser(UserDto userDto, long userId) {
        return patch("/" + userId, userId, userDto);
    }

    public Object getUser(long userId) {
        return get("/" + userId, userId);
    }

    public Object getAll() {
        return get("", 0);
    }

    public Object deleteUser(long userId) {
        return delete("/" + userId, userId);
    }
}

