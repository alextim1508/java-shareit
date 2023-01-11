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
public class ShortBookingDtoTest extends BookingBaseTest {

    @Autowired
    private JacksonTester<ShortBookingDtoOut> jacksonTester;

    @Test
    void toItemDto() throws IOException {
        JsonContent<ShortBookingDtoOut> result = jacksonTester.write(shortBookingDtoOut);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(shortBookingDtoOut.getId());
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(shortBookingDtoOut.getBookerId());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(shortBookingDtoOut.getStartDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(shortBookingDtoOut.getEndDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    void equalsAndHashCodeTest() {
        ShortBookingDtoOut x = ShortBookingDtoOut.builder()
                .id(booking.getId())
                .bookerId(booker.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();

        ShortBookingDtoOut y = ShortBookingDtoOut.builder()
                .id(booking.getId())
                .bookerId(booker.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();

        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void equals_shouldReturnFalseWhenIdsAreNotTheSame() {
        ShortBookingDtoOut x = ShortBookingDtoOut.builder()
                .id(booking.getId())
                .bookerId(booker.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();

        ShortBookingDtoOut y = ShortBookingDtoOut.builder()
                .id(-1)
                .bookerId(booker.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseWhenBookerIdsAreNotTheSame() {
        ShortBookingDtoOut x = ShortBookingDtoOut.builder()
                .id(booking.getId())
                .bookerId(booker.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();

        ShortBookingDtoOut y = ShortBookingDtoOut.builder()
                .id(booking.getId())
                .bookerId(-1)
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseWhenStartDatesAreNotTheSame() {
        ShortBookingDtoOut x = ShortBookingDtoOut.builder()
                .id(booking.getId())
                .bookerId(booker.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();

        ShortBookingDtoOut y = ShortBookingDtoOut.builder()
                .id(booking.getId())
                .bookerId(booker.getId())
                .startDate(booking.getStartDate().plusDays(1))
                .endDate(booking.getEndDate())
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseWhenEndDatesAreNotTheSame() {
        ShortBookingDtoOut x = ShortBookingDtoOut.builder()
                .id(booking.getId())
                .bookerId(booker.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();

        ShortBookingDtoOut y = ShortBookingDtoOut.builder()
                .id(booking.getId())
                .bookerId(booker.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate().plusDays(1))
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equalsTest() {
        assertThat(shortBookingDtoOut.equals(shortBookingDtoOut)).isTrue();
        assertThat(shortBookingDtoOut.equals(null)).isFalse();
        assertThat(shortBookingDtoOut.equals(new Object())).isFalse();
    }

    @Test
    void noArgsConstructorTest() {
        ShortBookingDtoOut bookingDto = new ShortBookingDtoOut();
        bookingDto.setId(booking.getId());
        bookingDto.setStartDate(booking.getStartDate());
        bookingDto.setEndDate(booking.getEndDate());
        bookingDto.setBookerId(booker.getId());

        assertThat(bookingDto.getId()).isEqualTo(booking.getId());
        assertThat(bookingDto.getBookerId()).isEqualTo(booker.getId());
        assertThat(bookingDto.getStartDate()).isEqualTo(booking.getStartDate());
        assertThat(bookingDto.getEndDate()).isEqualTo(booking.getEndDate());
    }

    @Test
    void allArgsConstructorTest() {
        ShortBookingDtoOut bookingDto = new ShortBookingDtoOut(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStartDate(),
                booking.getEndDate());

        assertThat(bookingDto.getId()).isEqualTo(booking.getId());
        assertThat(bookingDto.getBookerId()).isEqualTo(booker.getId());
        assertThat(bookingDto.getStartDate()).isEqualTo(booking.getStartDate());
        assertThat(bookingDto.getEndDate()).isEqualTo(booking.getEndDate());
    }

    @Test
    void toStringTest() {
        assertThat(shortBookingDtoOut.toString()).startsWith(shortBookingDtoOut.getClass().getSimpleName());
    }
}