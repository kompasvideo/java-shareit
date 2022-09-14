package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
            builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .build()
        );
    }

    public Object add(ItemDto itemDto, long userId) {
        return post("", userId, itemDto);
    }

    public Object updateItem(ItemDto itemDto, long itemId, long userId) {
        return patch("/" + itemId, userId, itemDto);
    }

    public Object getItem(long userId, long itemId) {
        return get("/" + itemId, userId);
    }

    public Object getAllItems(long userId, long from, long size) {
        Map<String, Object> parameters = Map.of(
            "from", from,
            "size", size
        );
        return get("?from={from}&size={size}", (long) userId, parameters);
    }

    public Object searchItem(String text, int from, int size, long userId) {
        Map<String, Object> parameters = Map.of(
            "text", text,
            "from", from,
            "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", (long) userId, parameters);
    }

    public Object createComment(CommentDto commentDto, long itemId, long userId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}
