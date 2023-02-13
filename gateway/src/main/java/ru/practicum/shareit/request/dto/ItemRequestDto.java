package ru.practicum.shareit.request.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.item.dto.Create;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ItemRequestDto {
    private Long id;
    @NotBlank(groups = Create.class)
    @Length(max = 200)
    private String description;
    private User requester;
    private LocalDateTime created;
    private List<ItemAnswerDto> items;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ItemAnswerDto {
        private Long id;
        private String name;
        private String description;
        private Long idOwner;
        private boolean available;
        private Long requestId;
    }
}
