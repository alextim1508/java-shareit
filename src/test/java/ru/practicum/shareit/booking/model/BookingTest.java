package ru.practicum.shareit.booking.model;


import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingBaseTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    void allArgsConstructorTest() {
        Booking booking = new Booking(
                this.booking.getId(),
                this.booking.getStartDate(),
                this.booking.getEndDate(),
                item,
                booker,
                this.booking.getStatus());

        assertThat(booking.getId()).isEqualTo(this.booking.getId());
        assertThat(booking.getStartDate()).isEqualTo(this.booking.getStartDate());
        assertThat(booking.getEndDate()).isEqualTo(this.booking.getEndDate());
        assertThat(booking.getItem()).isEqualTo(item);
        assertThat(booking.getBooker()).isEqualTo(booker);
        assertThat(booking.getStatus()).isEqualTo(this.booking.getStatus());
    }

    @Test
    void allArgsConstructor_shouldThrowNotFoundExceptionWhenStartDateIsNull() {
        assertThatThrownBy(() -> {
            Booking booking = new Booking(
                    this.booking.getId(),
                    null,
                    this.booking.getEndDate(),
                    item,
                    booker,
                    this.booking.getStatus());
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("startDate is marked non-null but is null");
    }

    @Test
    void allArgsConstructor_shouldThrowNotFoundExceptionWhenEndDateIsNull() {
        assertThatThrownBy(() -> {
            Booking booking = new Booking(
                    this.booking.getId(),
                    this.booking.getStartDate(),
                    null,
                    item,
                    booker,
                    this.booking.getStatus());
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("endDate is marked non-null but is null");
    }

    @Test
    void allArgsConstructor_shouldThrowNotFoundExceptionWhenItemIsNull() {
        assertThatThrownBy(() -> {
            Booking booking = new Booking(
                    this.booking.getId(),
                    this.booking.getStartDate(),
                    this.booking.getEndDate(),
                    null,
                    booker,
                    this.booking.getStatus());
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("item is marked non-null but is null");
    }

    @Test
    void allArgsConstructor_shouldThrowNotFoundExceptionWhenBookerIsNull() {
        assertThatThrownBy(() -> {
            Booking booking = new Booking(
                    this.booking.getId(),
                    this.booking.getStartDate(),
                    this.booking.getEndDate(),
                    item,
                    null,
                    this.booking.getStatus());
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("booker is marked non-null but is null");
    }

    @Test
    void builder_shouldReturnNotNull() {
        Booking.BookingBuilder builder = Booking.builder()
                .id(booking.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .item(item)
                .booker(booker);

        assertThat(builder.toString()).contains(builder.getClass().getSimpleName());
        assertThat(builder.build()).isNotNull();
    }

    @Test
    void builder_shouldThrowNotFoundExceptionWhenStartDateIsNull() {
        assertThatThrownBy(() -> {
            Booking.builder()
                    .id(booking.getId())
                    .startDate(null)
                    .endDate(booking.getEndDate())
                    .item(item)
                    .booker(booker);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("startDate is marked non-null but is null");
    }

    @Test
    void builder_shouldThrowNotFoundExceptionWhenEndDateIsNull() {
        assertThatThrownBy(() -> {
            Booking.builder()
                    .id(booking.getId())
                    .startDate(booking.getStartDate())
                    .endDate(null)
                    .item(item)
                    .booker(booker);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("endDate is marked non-null but is null");
    }

    @Test
    void builder_shouldThrowNotFoundExceptionWhenItemIsNull() {
        assertThatThrownBy(() -> {
            Booking.builder()
                    .id(booking.getId())
                    .startDate(booking.getStartDate())
                    .endDate(booking.getEndDate())
                    .item(null)
                    .booker(booker);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("item is marked non-null but is null");
    }

    @Test
    void builder_shouldThrowNotFoundExceptionWhenBookerIsNull() {
        assertThatThrownBy(() -> {
            Booking.builder()
                    .id(booking.getId())
                    .startDate(booking.getStartDate())
                    .endDate(booking.getEndDate())
                    .item(item)
                    .booker(null);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("booker is marked non-null but is null");
    }


    @Test
    void setName_shouldThrowNotFoundExceptionWhenStartDateIsNull() {
        Booking.BookingBuilder booking = Booking.builder()
                .id(this.booking.getId())
                .startDate(this.booking.getStartDate())
                .endDate(this.booking.getEndDate())
                .item(item)
                .booker(booker);

        assertThatThrownBy(() -> {
            booking.startDate(null);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("startDate is marked non-null but is null");
    }

    @Test
    void setName_shouldThrowNotFoundExceptionWhenEndDateIsNull() {
        Booking.BookingBuilder booking = Booking.builder()
                .id(this.booking.getId())
                .startDate(this.booking.getStartDate())
                .endDate(this.booking.getEndDate())
                .item(item)
                .booker(booker);

        assertThatThrownBy(() -> {
            booking.endDate(null);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("endDate is marked non-null but is null");
    }

    @Test
    void setName_shouldThrowNotFoundExceptionWhenItemIsNull() {
        Booking.BookingBuilder booking = Booking.builder()
                .id(this.booking.getId())
                .startDate(this.booking.getStartDate())
                .endDate(this.booking.getEndDate())
                .item(item)
                .booker(booker);

        assertThatThrownBy(() -> {
            booking.item(null);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("item is marked non-null but is null");
    }

    @Test
    void setName_shouldThrowNotFoundExceptionWhenBookerIsNull() {
        Booking.BookingBuilder booking = Booking.builder()
                .id(this.booking.getId())
                .startDate(this.booking.getStartDate())
                .endDate(this.booking.getEndDate())
                .item(item)
                .booker(booker);

        assertThatThrownBy(() -> {
            booking.booker(null);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("booker is marked non-null but is null");
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