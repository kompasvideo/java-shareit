package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String HEADER_REQUEST = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingCreateDto createNewBooking(@RequestBody BookingCreateDto bookingCreateDto,
                                             @RequestHeader(HEADER_REQUEST) Long userId) {
        return bookingService.create(bookingCreateDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto setStatusBooking(@RequestHeader(HEADER_REQUEST) Long userId,
                                       @PathVariable("bookingId") Long bookingId,
                                       @RequestParam(value = "approved", required = false) Boolean approved) {
        return bookingService.setStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@RequestHeader(HEADER_REQUEST) Long userId,
                               @PathVariable("bookingId") Long bookingId) {
        return bookingService.findById(userId, bookingId);
    }

    @GetMapping()
    public List<BookingDto> findAllForUser(@RequestHeader(HEADER_REQUEST) Long userId,
                                           @RequestParam(value = "state", required = false) State state) {
        return bookingService.findAllForUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllForOwner(@RequestHeader(HEADER_REQUEST) Long userId,
                                            @RequestParam(value = "state", required = false) State state) {
        return bookingService.findAllForOwner(userId, state);
    }
}

