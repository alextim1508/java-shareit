package ru.practicum.shareit.booking.dto;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingBaseTest;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoInTest extends BookingBaseTest {

    @Autowired
    private JacksonTester<BookingDtoIn> jacksonTester;

    @Test
    void toItemDto() throws IOException {
        JsonContent<BookingDtoIn> result = jacksonTester.write(bookingDtoIn);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(bookingDtoIn.getItemId());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(bookingDtoIn.getStartDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingDtoIn.getEndDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    void equalsAndHashCodeTest() {
        BookingDtoIn x = BookingDtoIn.builder()
                .itemId(booking.getItem().getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();

        BookingDtoIn y = BookingDtoIn.builder()
                .itemId(booking.getItem().getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();


        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void equalsTest() {
        assertThat(item.equals(item)).isTrue();
        assertThat(item.equals(null)).isFalse();
        assertThat(item.equals(new Object())).isFalse();
    }

    @Test
    void noArgsConstructorTest() {
        BookingDtoIn bookingDto = new BookingDtoIn();
        bookingDto.setItemId(booking.getId());
        bookingDto.setStartDate(booking.getStartDate());
        bookingDto.setEndDate(booking.getEndDate());

        assertThat(bookingDto.getItemId()).isEqualTo(booking.getItem().getId());
        assertThat(bookingDto.getStartDate()).isEqualTo(booking.getStartDate());
        assertThat(bookingDto.getEndDate()).isEqualTo(booking.getEndDate());
    }

    @Test
    void allArgsConstructorTest() {
        BookingDtoIn bookingDto = new BookingDtoIn(
                booking.getId(),
                booking.getStartDate(),
                booking.getEndDate());

        assertThat(bookingDto.getItemId()).isEqualTo(booking.getItem().getId());
        assertThat(bookingDto.getStartDate()).isEqualTo(booking.getStartDate());
        assertThat(bookingDto.getEndDate()).isEqualTo(booking.getEndDate());
    }

    @Test
    void toStringTest() {
        assertThat(bookingDtoIn.toString()).startsWith(bookingDtoIn.getClass().getSimpleName());
    }
}