package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/**
 * // Контролёр для вещей
 */

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                       @RequestBody ItemDto itemDto) {
        return itemService.saveItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public OwnerItemDto getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                @PathVariable long itemId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping()
    public List<OwnerItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("controller getAllItems");
        return itemService.getAllItem(userId);
    }


    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestParam String text) {
        return itemService.searchItem(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody Comment comment,
                                    @RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable("itemId") long itemId) {
        return itemService.addComment(userId, itemId, comment);
    }
}
