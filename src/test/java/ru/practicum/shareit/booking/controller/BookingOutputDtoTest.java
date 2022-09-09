package ru.practicum.shareit.booking.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.controller.ItemDto;
import ru.practicum.shareit.user.controller.UserDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;

@JsonTest
public class BookingOutputDtoTest {

    @Autowired
    private JacksonTester<BookingOutputDto> jacksonTester;

    @Test
    void toItemDto() throws IOException {
        LocalDateTime now = LocalDateTime.now();

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1)
                .build();

        BookingOutputDto bookingDto = BookingOutputDto.builder()
                .id(1)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .item(itemDto)
                .booker(new UserDto(1, "booker", "booker@gmail.com"))
                .status(WAITING)
                .build();

        JsonContent<BookingOutputDto> result = jacksonTester.write(bookingDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(now.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(now.plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("booker");
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo("booker@gmail.com");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.item.requestId").isEqualTo(1);

    }
}
