package ru.practicum.shareit.booking.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.item.model.Item;
import ru.practicum.shareit.user.User;

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
    public Item add(@RequestHeader("X-Sharer-User-Id") long userId,
                            @RequestBody Item item) {
        return itemService.saveItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                           @RequestBody Item item) {
        return itemService.updateItem(userId,itemId, item);
    }

    @GetMapping("/{itemId}")
    public Item getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId){
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    public List<Item> getAllItem(@RequestHeader("X-Sharer-User-Id") long userId){
        return itemService.getAllItem(userId);
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text){
        return itemService.searchItem(userId, text);
    }
}
