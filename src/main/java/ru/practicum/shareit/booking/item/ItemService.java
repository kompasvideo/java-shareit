package ru.practicum.shareit.booking.item;

import ru.practicum.shareit.booking.item.dto.CommentDto;
import ru.practicum.shareit.booking.item.dto.ItemDto;
import ru.practicum.shareit.booking.item.dto.ItemFoundDto;
import ru.practicum.shareit.booking.item.model.Comment;


import java.util.List;

public interface ItemService {
    // методы для создания предмета
    ItemDto create(ItemDto itemDto, Long userId);

    // метод для обновления предмета
    ItemDto update(ItemDto itemDto, Long userId, Long itemId);

    // метод для поиска предмета по id пользователя и по id предмета
    ItemFoundDto findByUserIdAndItemId(Long userId, Long itemId);

    // метод для поиска предметов по id пользователя
    List<ItemFoundDto> findAllItemsByUserId(Long userId);

    // метод для создания отзыва к предмету
    CommentDto addComment(Long userId, Long itemId, Comment comment);

    // метод для поиска предмета по тексту
    List<ItemDto> findItemByText(Long userId, String text);


}
