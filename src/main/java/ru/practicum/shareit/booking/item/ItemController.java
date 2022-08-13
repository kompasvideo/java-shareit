package ru.practicum.shareit.booking.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.item.dto.*;
import ru.practicum.shareit.booking.item.model.Comment;
import ru.practicum.shareit.booking.item.model.Item;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * // Контролёр для вещей
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String HEADER_REQUEST = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    public ItemDto createNewItem(@RequestBody ItemDto itemDto,
                                 @RequestHeader(HEADER_REQUEST) Long userId) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @RequestHeader(HEADER_REQUEST) Long userId,
                              @PathVariable("itemId") Long itemId) {
        return itemService.update(itemDto, userId, itemId);
    }

    @GetMapping()
    public List<ItemFoundDto> getAllItems(@RequestHeader(HEADER_REQUEST) Long userId) {
        return itemService.findAllItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemFoundDto findItemByItemId(@RequestHeader(HEADER_REQUEST) Long userId,
                                         @PathVariable("itemId") Long itemId) {
        return itemService.findByUserIdAndItemId(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemByText(@RequestHeader(HEADER_REQUEST) Long userId,
                                        @RequestParam(name = "text") String text) {
        return itemService.findItemByText(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody Comment comment,
                                    @RequestHeader(HEADER_REQUEST) Long userId,
                                    @PathVariable("itemId") Long itemId) {
        return itemService.addComment(userId, itemId, comment);
    }
}
