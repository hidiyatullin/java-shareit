package ru.practicum.shareit.request.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.item.dto.Create;

import javax.validation.constraints.NotBlank;

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
}
