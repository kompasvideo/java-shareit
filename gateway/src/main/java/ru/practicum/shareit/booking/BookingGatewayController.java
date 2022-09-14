package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingGatewayController {

    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public Object get(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long bookingId) {
        log.info("Get booking id: {} by user id: {}", bookingId, userId);
        return bookingClient.get(userId, bookingId);
    }

    @PostMapping
    public Object create(@Valid @RequestBody BookingDto bookingDto,
                                   @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Add new booking by user id: {} to item id: {}", userId, bookingDto.getItemId());
        return bookingClient.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Object update(@RequestHeader("X-Sharer-User-Id") long userId,
                             @PathVariable long bookingId,
                             @RequestParam(required = false) Boolean approved) {
        log.info("Change availability by user id: {} to booking id: {}", userId, bookingId);
        return bookingClient.update(bookingId, userId, approved);
    }


    @GetMapping()
    public Object getAllByCurrentUser(@RequestHeader("X-Sharer-User-Id") long userId,
                             @RequestParam(required = false) String state,
                             @Valid @Positive @RequestParam(required = false, defaultValue = "0") Integer from,
                             @Valid @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Get bookings by user id: {}", userId);
        return bookingClient.getAllByCurrentUser("", userId, state, from, size);
    }

    @GetMapping("/owner")
    public Object getAllByOwnedItems(@RequestHeader("X-Sharer-User-Id") long userId,
                             @RequestParam(required = false) String state,
                             @Valid @Positive @RequestParam(required = false, defaultValue = "0") int from,
                             @Valid @Positive @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Get bookings by owner id: {}", userId);
        return bookingClient.getAllByCurrentUser("/owner", userId, state, from, size);
    }
}
