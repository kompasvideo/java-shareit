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
    private Book booker;

    public static class Item {
        public long id;
        public String name;

        public Item(long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static class Book {
        public long id;

        public Book(long id) {
            this.id = id;
        }
    }
}

