package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemGatewayController {
    private final ItemClient itemClient;

    @PostMapping
    public Object add(@RequestHeader("X-Sharer-User-Id") long userId,
                      @Valid @RequestBody ItemDto itemDto) {
        log.info("Add new item by owner - user id: {}", userId);
        return itemClient.add(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public Object updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("Update item id: {} by owner - user id: {}", itemId, userId);
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public Object getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                @PathVariable long itemId) {
        log.info("Get item id: {}", itemId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping()
    public Object getAllItems(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                              @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Get user's id: {} items", userId);
        return itemClient.getAllItems(userId, from, size);
    }


    @GetMapping("/search")
    public Object searchItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestParam String text,
                                    @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                    @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Search items by '{}'", text);
        return itemClient.searchItem(text, from, size, userId);
    }

    @PostMapping("/{itemId}/comment")
    public Object createComment(@Valid @RequestBody CommentDto commentDto,
                                    @RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable long itemId) {
        log.info("Add new comment by user id: {} to item id: {}", itemId, userId);
        return itemClient.createComment(commentDto, itemId, userId);
    }
}
