package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto get(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long bookingId) {
        return bookingService.get(userId, bookingId);
    }

    @PostMapping
    public BookingCreateDto create(@RequestBody BookingCreateDto createBookingDto,
                                   @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.save(createBookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                             @PathVariable long bookingId,
                             @RequestParam(required = false) Boolean approved) {
        return bookingService.update(userId, bookingId, approved);
    }


    @GetMapping()
    public List<BookingDto> getAllByCurrentUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(required = false) String state,
                                                @RequestParam(required = false, defaultValue = "0") Integer from,
                                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        return bookingService.getAllByCurrentUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwnedItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(required = false) String state,
                                               @RequestParam(required = false, defaultValue = "0") int from,
                                               @RequestParam(required = false, defaultValue = "10") int size) {
        return bookingService.getAllByOwnedItems(userId, state, from, size);
    }
}


