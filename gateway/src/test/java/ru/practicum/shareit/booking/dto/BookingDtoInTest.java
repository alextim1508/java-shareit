package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
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
public class BookingDtoInTest  {

    @Autowired
    private JacksonTester<BookingDtoIn> jacksonTester;

    LocalDateTime now;
    BookingDtoIn bookingDtoIn;

    @BeforeEach
    protected void setUp() {
        now = LocalDateTime.now();
        bookingDtoIn = BookingDtoIn.builder()
                .itemId(1)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build();
    }

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
                .itemId(1)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build();

        BookingDtoIn y = BookingDtoIn.builder()
                .itemId(1)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build();


        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void equals_shouldReturnFalseWhenItemIdsAreNotTheSame() {
        BookingDtoIn x = BookingDtoIn.builder()
                .itemId(1)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build();

        BookingDtoIn y = BookingDtoIn.builder()
                .itemId(-1)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseWhenStartDatesAreNotTheSame() {
        BookingDtoIn x = BookingDtoIn.builder()
                .itemId(1)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build();

        BookingDtoIn y = BookingDtoIn.builder()
                .itemId(-1)
                .startDate(now.plusDays(2))
                .endDate(now.plusDays(2))
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseWhenEndDatesAreNotTheSame() {
        BookingDtoIn x = BookingDtoIn.builder()
                .itemId(1)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build();

        BookingDtoIn y = BookingDtoIn.builder()
                .itemId(-1)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(3))
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equalsTest() {
        assertThat(bookingDtoIn.equals(bookingDtoIn)).isTrue();
        assertThat(bookingDtoIn.equals(null)).isFalse();
        assertThat(bookingDtoIn.equals(new Object())).isFalse();
    }

    @Test
    void noArgsConstructorTest() {
        BookingDtoIn bookingDto = new BookingDtoIn();
        bookingDto.setItemId(bookingDtoIn.getItemId());
        bookingDto.setStartDate(bookingDtoIn.getStartDate());
        bookingDto.setEndDate(bookingDtoIn.getEndDate());

        assertThat(bookingDto.getItemId()).isEqualTo(bookingDtoIn.getItemId());
        assertThat(bookingDto.getStartDate()).isEqualTo(bookingDtoIn.getStartDate());
        assertThat(bookingDto.getEndDate()).isEqualTo(bookingDtoIn.getEndDate());
    }

    @Test
    void allArgsConstructorTest() {
        BookingDtoIn bookingDto = new BookingDtoIn(
                bookingDtoIn.getItemId(),
                bookingDtoIn.getStartDate(),
                bookingDtoIn.getEndDate());

        assertThat(bookingDto.getItemId()).isEqualTo(bookingDtoIn.getItemId());
        assertThat(bookingDto.getStartDate()).isEqualTo(bookingDtoIn.getStartDate());
        assertThat(bookingDto.getEndDate()).isEqualTo(bookingDtoIn.getEndDate());
    }

    @Test
    void toStringTest() {
        assertThat(bookingDtoIn.toString()).startsWith(bookingDtoIn.getClass().getSimpleName());
    }
}