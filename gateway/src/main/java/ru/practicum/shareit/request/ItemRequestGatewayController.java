package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestGatewayController {

    private final ItemRequestClient requestClient;

    @PostMapping
    public Object addNewRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("ItemRequestClient addNewRequest ");
        return requestClient.addNewRequest(itemRequestDto, userId);
    }

    @GetMapping
    public Object getListRequest(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("ItemRequestClient addNewRequest ");
        return requestClient.getListRequest(userId);
    }

    @GetMapping("/all")
    public Object getListRequestAllUsers(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                         @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("ItemRequestClient getListRequestAllUsers");
        return requestClient.getListRequestAllUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public Object getOneRequest(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        log.info("ItemRequestClient getOneRequest");
        return requestClient.getOneRequest(userId, requestId);
    }
}
