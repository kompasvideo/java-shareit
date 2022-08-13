package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.Status;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private Item item;
    private Booker booker;

    public static class Item {
        public Long id;
        public String name;

        public Item(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static class Booker {
        public Long id;

        public Booker(Long id) {
            this.id = id;
        }
    }
}

