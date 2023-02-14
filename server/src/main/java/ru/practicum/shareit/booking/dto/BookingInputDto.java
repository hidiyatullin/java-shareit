package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingInputDto {
    private long id;
    private long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
