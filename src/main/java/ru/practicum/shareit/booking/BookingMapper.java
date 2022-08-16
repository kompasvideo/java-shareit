package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Optional;

public class BookingMapper {

    public static BookingCreateDto toBookingCreateDto(Booking booking) {
        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setId(booking.getId());
        bookingCreateDto.setItemId(booking.getItem().getId());
        bookingCreateDto.setStart(booking.getStart());
        bookingCreateDto.setEnd(booking.getEnd());
        return bookingCreateDto;
    }

    public static BookingDto toBookingDto(Booking booking, BookingDto.Item item, BookingDto.Book booker) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setItem(item);
        bookingDto.setBooker(booker);
        return bookingDto;
    }

    public static Booking toBooking(BookingCreateDto bookingCreateDto, Optional<User> user,
                                    Optional<Item> item) {
        Booking booking = new Booking();
        booking.setStart(bookingCreateDto.getStart());
        booking.setEnd(bookingCreateDto.getEnd());
        booking.setItem( item.get());
        booking.setBooker(user.get());
        return booking;
    }

}

