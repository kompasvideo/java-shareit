package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testUserDto() throws Exception {
        LocalDateTime start = LocalDateTime.of(LocalDate.of(2022, 8, 26),
            LocalTime.of(12, 00));
        LocalDateTime end = LocalDateTime.of(LocalDate.of(2022, 8, 26),
            LocalTime.of(13, 00));

        Item item = new Item();
        item.setId(1L);
        item.setName("itemName");
        User user = new User();
        user.setId(1L);
        user.setName("userName");
        BookingDto bookingDto = new BookingDto(1L, start, end, item, user, Status.APPROVED);
        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2022-08-26T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2022-08-26T13:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("itemName");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("userName");
    }
}
