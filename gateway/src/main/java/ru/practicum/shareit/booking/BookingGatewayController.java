package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingGatewayController {

    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public Object get(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("BookingGatewayController get bookingId: {}, userId: {}", bookingId, userId);
        return bookingClient.get(userId, bookingId);
    }

    @PostMapping
    public Object create(@Valid @RequestBody BookingDto bookingDto,
                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("BookingGatewayController create, userId: {}, itemId: {}", userId, bookingDto.getItemId());
        return bookingClient.save(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Object update(@PathVariable long bookingId, @RequestParam Boolean approved,
                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("BookingGatewayController update userId: {}, bookingId: {}", userId, bookingId);
        return bookingClient.update(bookingId, userId, approved);
    }


    @GetMapping
    public Object getAllByCurrentUser(@RequestParam(defaultValue = "ALL") BookingState state,
                                      @RequestHeader("X-Sharer-User-Id") long userId,
                                      @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                      @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("BookingGatewayController getAllByCurrentUser userId: {}", userId);
        return bookingClient.getBookings("", userId, state, from, size);
    }

    @GetMapping("/owner")
    public Object getAllByOwnedItems(@RequestParam(defaultValue = "ALL") BookingState state,
                                     @RequestHeader("X-Sharer-User-Id") long userId,
                                     @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                     @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("BookingGatewayController getAllByOwnedItems userId: {}", userId);
        return bookingClient.getBookings("/owner", userId, state, from, size);
    }
}
