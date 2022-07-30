package ru.practicum.shareit.booking.item;

import ru.practicum.shareit.booking.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item save(long userId, Item item);

    Item updateItem(long userId, long itemId, Item item);

    Item getItem(long userId, long itemId);

    List<Item> getAllItem(long userId);

    List<Item> searchItem(long userId, String text);
}
