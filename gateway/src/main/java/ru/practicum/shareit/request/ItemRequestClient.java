package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
            builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/requests"))
                .build()
        );
    }

    public Object addNewRequest(ItemRequestDto itemRequestDto, long userId) {
        return post("", userId, itemRequestDto);
    }

    public Object getOneRequest(long userId, long requestId) {
        return get("/" + requestId, userId);
    }

    public Object getListRequest(long userId) {
        return get("", userId);
    }

    public Object getListRequestAllUsers(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
            "from", from,
            "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }
}
