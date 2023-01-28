package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@JsonTest
class ItemDtoTest {

    private JacksonTester<ItemDto> json;
    private ObjectMapper mapper;

    @Autowired
    public ItemDtoTest(JacksonTester<ItemDto> json, ObjectMapper mapper) {
        this.json = json;
        this.mapper = mapper;
    }

    @Test
    void testItemDto() throws Exception {
        LocalDateTime startLastBooking = LocalDateTime.now().minusDays(7);
        LocalDateTime endLastBooking = LocalDateTime.now().minusDays(3);
        LocalDateTime startNextBooking = LocalDateTime.now().plusDays(3);
        LocalDateTime endNextBooking = LocalDateTime.now().plusDays(7);
        ItemDto.BookingDto lastBooking = new ItemDto.BookingDto(1L, startLastBooking, endLastBooking, 1L);
        ItemDto.BookingDto nextBooking = new ItemDto.BookingDto(2L, startNextBooking, endNextBooking, 2L);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String startLastBookingString = mapper.writeValueAsString(startLastBooking);

        ItemDto itemDto = new ItemDto(1L, "itemName", "itemDescription", true, 0L, lastBooking,
                nextBooking, null);
        JsonContent<ItemDto> result = json.write(itemDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDto.getAvailable());
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.start").isEqualTo(startLastBookingString.replace("\"", ""));
        assertThat(result).extractingJsonPathStringValue("$.comments").isEqualTo(itemDto.getComments());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(itemDto.getRequestId().intValue());
    }
}