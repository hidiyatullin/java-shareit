package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
                            //Сделать свою аннотоацию, чтобы старт был раньше енд
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {
    private long id;
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
    @NotBlank(groups = Create.class)
    private ItemInput item;
    private UserInput booker;
    private Status status;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ItemInput {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserInput {
        private Long id;
    }

}
