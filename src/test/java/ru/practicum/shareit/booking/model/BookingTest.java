package ru.practicum.shareit.booking.model;


import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingBaseTest;


import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.booking.model.BookingStatus.*;

public class BookingTest extends BookingBaseTest {

    @Test
    void equalsAndHashCodeTest() {
        Booking x = Booking.builder()
                .id(booking.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .item(item)
                .booker(booker)
                .build();

        Booking y = Booking.builder()
                .id(booking.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .item(item)
                .booker(booker)
                .build();

        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void equalsTest() {
        assertThat(booking.equals(booking)).isTrue();
        assertThat(booking.equals(null)).isFalse();
        assertThat(booking.equals(new Object())).isFalse();
    }

    @Test
    void noArgsConstructorTest() {
        Booking booking = new Booking();
        booking.setId(this.booking.getId());
        booking.setStartDate(this.booking.getStartDate());
        booking.setEndDate(this.booking.getEndDate());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(this.booking.getStatus());

        assertThat(booking.getId()).isEqualTo(this.booking.getId());
        assertThat(booking.getStartDate()).isEqualTo(this.booking.getStartDate());
        assertThat(booking.getEndDate()).isEqualTo(this.booking.getEndDate());
        assertThat(booking.getItem()).isEqualTo(item);
        assertThat(booking.getBooker()).isEqualTo(booker);
        assertThat(booking.getStatus()).isEqualTo(this.booking.getStatus());
    }

    @Test
    void toStringTest() {
        assertThat(booking.toString()).startsWith(booking.getClass().getSimpleName());
    }

    @Test
    void bookingStatusTest() {
        assertThat(WAITING.getTitle()).isEqualTo("новое бронирование, ожидает одобрения");
        assertThat(APPROVED.getTitle()).isEqualTo("бронирование подтверждено владельцем");
        assertThat(REJECTED.getTitle()).isEqualTo("бронирование отклонено владельцем");
        assertThat(CANCELED.getTitle()).isEqualTo("бронирование отменено создателем");
    }
}