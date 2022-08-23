package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {
    ItemDto saveItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    OwnerItemDto getItem(long userId, long itemId);

    List<OwnerItemDto> getAllItem(long userId);

    List<ItemDto> searchItem(long userId, String text);

    CommentDto addComment(long userId, long itemId, Comment comment);
}
