package ru.practicum.shareit.booking.item;

import ru.practicum.shareit.booking.item.dto.CommentDto;
import ru.practicum.shareit.booking.item.dto.ItemDto;
import ru.practicum.shareit.booking.item.dto.ItemFoundDto;
import ru.practicum.shareit.booking.item.model.Comment;

import java.util.List;

public interface ItemService {
    ItemDto saveItem(long userId, ItemDto itemDto);

    ItemDto update(ItemDto itemDto, Long userId, Long itemId);

    ItemFoundDto findByUserIdAndItemId(Long userId, Long itemId);

    List<ItemFoundDto> findAllItemsByUserId(Long userId);

    CommentDto addComment(Long userId, Long itemId, Comment comment);

    List<ItemDto> findItemByText(Long userId, String text);
}
