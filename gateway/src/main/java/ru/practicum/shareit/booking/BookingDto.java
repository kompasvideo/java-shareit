package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Status;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long id;

    @FutureOrPresent
    private LocalDateTime start;

    @Future
    private LocalDateTime end;
    //private Item item;
    //private User booker;
    private int itemId;
    private int bookerId;
    private Status status;
}