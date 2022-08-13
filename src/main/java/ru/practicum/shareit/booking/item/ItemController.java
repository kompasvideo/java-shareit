package ru.practicum.shareit.booking.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.item.dto.CommentDto;
import ru.practicum.shareit.booking.item.dto.ItemDto;
import ru.practicum.shareit.booking.item.dto.ItemFoundDto;
import ru.practicum.shareit.booking.item.model.Comment;

import java.util.List;

/**
 * // Контролёр для вещей
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                       @RequestBody ItemDto itemDto) {
        return itemService.saveItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable("itemId") Long itemId) {
        return itemService.update(itemDto, userId, itemId);
    }

    @GetMapping()
    public List<ItemFoundDto> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAllItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemFoundDto findItemByItemId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable("itemId") Long itemId) {
        return itemService.findByUserIdAndItemId(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemByText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam(name = "text") String text) {
        return itemService.findItemByText(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody Comment comment,
                                    @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable("itemId") Long itemId) {
        return itemService.addComment(userId, itemId, comment);
    }
}
