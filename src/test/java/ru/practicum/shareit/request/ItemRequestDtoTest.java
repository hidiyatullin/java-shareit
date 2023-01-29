package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestDtoTest {

    private JacksonTester<ItemRequestDto> json;
    private ObjectMapper mapper;
    private User user;

    @Autowired
    public ItemRequestDtoTest(JacksonTester<ItemRequestDto> json, ObjectMapper mapper) {
        this.json = json;
        this.mapper = mapper;
    }

    @Test
    public void testItemRequestDto() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "itemRequestDescription", user, LocalDateTime.now(), null);
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemRequestDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestDto.getDescription());
    }
}