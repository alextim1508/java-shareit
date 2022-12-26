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
public class BookingDtoOutTest extends BookingBaseTest {

    @Autowired
    private JacksonTester<BookingDtoOut> jacksonTester;

    @Test
    void toItemDto() throws IOException {
        JsonContent<BookingDtoOut> result = jacksonTester.write(bookingDtoOut);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(bookingDtoOut.getId());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(bookingDtoOut.getStartDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingDtoOut.getEndDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(bookingDtoOut.getStatus().name());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(bookingDtoOut.getBooker().getId());
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo(bookingDtoOut.getBooker().getName());
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo(bookingDtoOut.getBooker().getEmail());
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(bookingDtoOut.getItem().getId());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(bookingDtoOut.getItem().getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo(bookingDtoOut.getItem().getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(bookingDtoOut.getItem().getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.item.requestId").isEqualTo(bookingDtoOut.getItem().getRequestId());
    }

    @Test
    void equalsAndHashCodeTest() {
        BookingDtoOut x = BookingDtoOut.builder()
                .id(booking.getId())
                .item(itemDtoOut)
                .booker(bookerDtoOut)
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .build();

        BookingDtoOut y = BookingDtoOut.builder()
                .id(booking.getId())
                .item(itemDtoOut)
                .booker(bookerDtoOut)
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .build();

        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void equalsTest() {
        assertThat(bookingDtoOut.equals(bookingDtoOut)).isTrue();
        assertThat(bookingDtoOut.equals(null)).isFalse();
        assertThat(bookingDtoOut.equals(new Object())).isFalse();
    }

    @Test
    void noArgsConstructorTest() {
        BookingDtoOut bookingDto = new BookingDtoOut();
        bookingDto.setId(booking.getId());
        bookingDto.setStartDate(booking.getStartDate());
        bookingDto.setEndDate(booking.getEndDate());
        bookingDto.setItem(itemDtoOut);
        bookingDto.setBooker(bookerDtoOut);
        bookingDto.setStatus(booking.getStatus());

        assertThat(bookingDto.getId()).isEqualTo(booking.getId());
        assertThat(bookingDto.getStartDate()).isEqualTo(booking.getStartDate());
        assertThat(bookingDto.getEndDate()).isEqualTo(booking.getEndDate());
        assertThat(bookingDto.getItem()).isEqualTo(itemDtoOut);
        assertThat(bookingDto.getBooker()).isEqualTo(bookerDtoOut);
        assertThat(bookingDto.getStatus()).isEqualTo(booking.getStatus());
    }

    @Test
    void allArgsConstructorTest() {
        BookingDtoOut bookingDto = new BookingDtoOut(
                booking.getId(),
                booking.getStartDate(),
                booking.getEndDate(),
                itemDtoOut,
                bookerDtoOut,
                booking.getStatus());

        assertThat(bookingDto.getId()).isEqualTo(booking.getId());
        assertThat(bookingDto.getStartDate()).isEqualTo(booking.getStartDate());
        assertThat(bookingDto.getEndDate()).isEqualTo(booking.getEndDate());
        assertThat(bookingDto.getItem()).isEqualTo(itemDtoOut);
        assertThat(bookingDto.getBooker()).isEqualTo(bookerDtoOut);
        assertThat(bookingDto.getStatus()).isEqualTo(booking.getStatus());
    }

    @Test
    void toStringTest() {
        assertThat(bookingDtoOut.toString()).startsWith(bookingDtoOut.getClass().getSimpleName());
    }
}
