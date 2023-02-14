package ru.practicum.shareit.item.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(of = "id")
public class ItemDto {
    private Long id;
    @NotBlank(groups = Create.class)
    @Length(max = 30, groups = Create.class)
    private String name;
    @NotBlank(groups = Create.class)
    @Length(max = 100, groups = Create.class)
    private String description;
    @NotNull(groups = Create.class)
    private Boolean available;
    private Long requestId;
}
