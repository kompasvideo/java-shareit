package ru.practicum.shareit.booking.item;

import ru.practicum.shareit.booking.item.dto.ItemDto;

import java.util.List;

interface ItemService {

    ItemDto saveItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemDto getItem(long userId, long itemId);

    List<ItemDto> getAllItem(long userId);

    List<ItemDto> searchItem(long userId, String text);
}
