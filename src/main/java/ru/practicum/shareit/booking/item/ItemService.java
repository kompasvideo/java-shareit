package ru.practicum.shareit.booking.item;

import ru.practicum.shareit.booking.item.dto.CommentDto;
import ru.practicum.shareit.booking.item.dto.ItemDto;
import ru.practicum.shareit.booking.item.dto.ItemFoundDto;
import ru.practicum.shareit.booking.item.model.Comment;

import java.util.List;

public interface ItemService {
    ItemDto saveItem(long userId, ItemDto itemDto);

    ItemDto update(long userId, long itemId, ItemDto itemDto);

    ItemFoundDto getItem(long userId, long itemId);

    List<ItemFoundDto> getAllItem(long userId);

    List<ItemDto> searchItem(long userId, String text);
    CommentDto addComment(Long userId, Long itemId, Comment comment);
}
