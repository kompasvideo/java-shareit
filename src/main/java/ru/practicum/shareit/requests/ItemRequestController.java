package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestInputDto;

import java.util.List;

/**
 * ласс запрос
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addNewRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestBody ItemRequestInputDto itemRequestInputDto) throws Throwable {
        return itemRequestService.addNewRequest(userId, itemRequestInputDto);
    }

    @GetMapping
    public List<ItemRequestDto> getListRequest(@RequestHeader("X-Sharer-User-Id") long userId) throws Throwable {
        return itemRequestService.getListRequest(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getListRequestAllUsers(@RequestHeader("X-Sharer-User-Id") long userId,
                                                       @RequestParam(required = false, defaultValue = "0") int from,
                                                       @RequestParam(required = false, defaultValue = "10") int size) throws Throwable {
        return itemRequestService.getListRequestAllUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getOneRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @PathVariable long requestId) throws Throwable {
        return itemRequestService.getOneRequest(userId, requestId);
    }
}


