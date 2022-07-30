package ru.practicum.shareit.booking.item;

import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.booking.item.model.Item;

import java.util.List;

interface ItemService {
    Item saveItem(long userId, Item item);
    Item updateItem(long userId, long itemId, Item item);
    Item getItem(long userId, long itemId);
    List<Item> getAllItem(long userId);
    List<Item> searchItem(long userId, String text);
}
