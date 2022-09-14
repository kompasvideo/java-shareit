package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestGatewayController {

    private final RequestClient requestClient;

    @PostMapping
    public Object addNewRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Add request");
        return requestClient.addNewRequest(itemRequestDto, userId);
    }

    @GetMapping
    public Object getListRequest(@RequestHeader("X-Sharer-User-Id") long userId)  {
        return requestClient.getListRequest(userId);
    }

    @GetMapping("/all")
    public Object getListRequestAllUsers(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Valid @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
                                         @Valid @Positive @RequestParam(required = false, defaultValue = "10") int size) {
        return requestClient.getListRequestAllUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public Object getOneRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @PathVariable long requestId) {
        return requestClient.getOneRequest(userId, requestId);
    }
}
