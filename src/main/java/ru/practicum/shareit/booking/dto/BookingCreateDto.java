package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingCreateDto {
    private Long id;
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}