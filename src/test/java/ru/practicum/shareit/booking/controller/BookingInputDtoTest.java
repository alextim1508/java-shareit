package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingInputDtoTest {

    @Autowired
    private JacksonTester<BookingInputDto> jacksonTester;

    @Test
    void toItemDto() throws IOException {
        LocalDateTime now = LocalDateTime.now();

        BookingInputDto bookingDto = new BookingInputDto(1, 2, now.plusDays(1), now.plusDays(2));

        JsonContent<BookingInputDto> result = jacksonTester.write(bookingDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(bookingDto.getId());
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(bookingDto.getItemId());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(bookingDto.getStartDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingDto.getEndDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }
}
