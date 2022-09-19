package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Slf4j
@Service
public class ItemClient extends BaseClient {

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
            builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/items"))
                .build()
        );
    }

    public Object add(ItemDto itemDto, long userId) {
        return post("", userId, itemDto);
    }

    public Object updateItem(ItemDto itemDto, int itemId, int userId) {
        return patch("/" + itemId, userId, itemDto);
    }

    public Object getItem(int userId, int itemId) {
        return get("/" + itemId, userId);
    }

    public Object getAllItem(long userId, int from, int size) {
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

    public Object addComment(CommentDto commentDto, long itemId, long userId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}