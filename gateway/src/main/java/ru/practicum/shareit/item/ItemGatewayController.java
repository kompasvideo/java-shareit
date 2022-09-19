package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemGatewayController {

    private final ItemClient itemClient;

    @PostMapping
    public Object add(@Valid @RequestBody ItemDto itemDto,
                      @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("ItemGatewayController add userId: {}", userId);
        return itemClient.add(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public Object updateItem(@RequestBody ItemDto itemDto,
                             @RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId) {
        log.info("ItemGatewayController updateItem itemId: {}, userId: {}", itemId, userId);
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public Object getItem(@PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("ItemGatewayController getItem itemId: {}", itemId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public Object getAllItems(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                              @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("ItemGatewayController getAllItems userId: {}", userId);
        return itemClient.getAllItem(userId, from, size);
    }

    @GetMapping("/search")
    public Object searchItem(@RequestHeader("X-Sharer-User-Id") long userId,
                             @RequestParam String text,
                             @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                             @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("ItemGatewayController searchItem  text = '{}'", text);
        return itemClient.searchItem(text, from, size, userId);
    }

    @PostMapping("/{itemId}/comment")
    public Object createComment(@Valid @RequestBody CommentDto commentDto, @PathVariable long itemId,
                                @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("ItemGatewayController createComment userId: {}, itemId: {}", itemId, userId);
        return itemClient.addComment(commentDto, itemId, userId);
    }
}
