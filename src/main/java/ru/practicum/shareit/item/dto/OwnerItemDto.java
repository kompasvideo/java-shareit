package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import ru.practicum.shareit.booking.Booking;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OwnerItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private LastBooking lastBooking;
    private NextBooking nextBooking;
    @Builder.Default
    private List<CommentDto> comments = new ArrayList<>();


    public static class LastBooking {
        public Long id;
        public Long bookerId;

        public LastBooking(Booking booking) {
            this.id = booking.getId();
            this.bookerId = booking.getBooker().getId();
        }
    }

    public static class NextBooking {
        public Long id;
        public Long bookerId;

        public NextBooking(Booking booking) {
            this.id = booking.getId();
            this.bookerId = booking.getBooker().getId();
        }
    }
}

