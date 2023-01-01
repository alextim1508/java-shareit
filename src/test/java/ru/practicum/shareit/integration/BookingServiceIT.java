package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingDtoOutAbs;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDtoIn;
import ru.practicum.shareit.user.dto.UserDtoOut;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
public class BookingServiceIT {

    @Autowired
    BookingService bookingService;

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;


    UserDtoOut savedBooker, savedOwner;
    ItemDtoOut savedItem;
    LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        UserDtoIn owner = UserDtoIn.builder()
                .name("owner")
                .email("owner@gmail.com")
                .build();

        ItemDtoIn itemDtoIn = ItemDtoIn.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        UserDtoIn booker = UserDtoIn.builder()
                .name("booker")
                .email("booker@gmail.com")
                .build();

        savedBooker = userService.create(booker);
        savedOwner = userService.create(owner);

        savedItem = (ItemDtoOut) itemService.create(itemDtoIn, savedOwner.getId());
    }

    @Test
    void should_saveBooking() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
                .itemId(savedItem.getId())
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build();

        bookingService.create(bookingDtoIn, savedBooker.getId());
    }

    @Test
    void should_throwException_when_savingItemIsNotAvailable() {
        ItemDtoIn itemDtoIn = ItemDtoIn.builder()
                .name("name2")
                .description("description2")
                .available(false)
                .build();
        ItemDtoOut savedItem = (ItemDtoOut) itemService.create(itemDtoIn, savedOwner.getId());

        BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
                .itemId(savedItem.getId())
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build();

        assertThatThrownBy(() -> {
            bookingService.create(bookingDtoIn, savedBooker.getId());
        }).isInstanceOf(ItemIsNotAvailableException.class);
    }

    @Test
    void should_throwException_when_ownerAndBookerAreTheSame() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
                .itemId(savedItem.getId())
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build();


        assertThatThrownBy(() -> {
            bookingService.create(bookingDtoIn, savedOwner.getId());
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    void should_throwException_when_bookingIsMissingInDB() {
        assertThatThrownBy(() -> {
            bookingService.getById(1, 1);
        }).isInstanceOf(NotFoundException.class);
    }


    @Test
    void should_getById() {
        BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
                .itemId(savedItem.getId())
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build();

        BookingDtoOut savedBooking = (BookingDtoOut) bookingService.create(bookingDtoIn, savedBooker.getId());


        assertThat(bookingService.getById(savedBooking.getId(), savedOwner.getId())).isEqualTo(savedBooking);
        assertThat(bookingService.getById(savedBooking.getId(), savedBooker.getId())).isEqualTo(savedBooking);
    }

    @Test
    void should_getAllByBooker() {
        for (int i = 1; i < 10; i++) {
            BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
                    .itemId(savedItem.getId())
                    .startDate(now.plusDays(i))
                    .endDate(now.plusDays(i + 1))
                    .build();

            bookingService.create(bookingDtoIn, savedBooker.getId());
        }

        List<? extends BookingDtoOutAbs> allByBooker = bookingService.getAllByBooker(savedBooker.getId(), 3, 2);

        assertThat(allByBooker.stream().map((Function<BookingDtoOutAbs, LocalDateTime>) bookingDtoOutAbs ->
                ((BookingDtoOut) bookingDtoOutAbs).getStartDate())).hasSameElementsAs(Arrays.asList(
                now.plusDays(6),
                now.plusDays(5)
        ));
    }

    @Test
    void should_getBookingByBookerAndStatus() {
        for (int i = 1; i < 10; i++) {
            BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
                    .itemId(savedItem.getId())
                    .startDate(now.plusDays(i))
                    .endDate(now.plusDays(i + 1))
                    .build();

            BookingDtoOut booking = (BookingDtoOut) bookingService.create(bookingDtoIn, savedBooker.getId());

            if (i % 2 == 0)
                bookingService.approve(booking.getId(), savedOwner.getId(), true);
        }

        List<? extends BookingDtoOutAbs> booking = bookingService.getBookingByBookerAndStatus(savedBooker.getId(), BookingStatus.APPROVED, 2, 2);

        assertThat(booking.stream().map((Function<BookingDtoOutAbs, LocalDateTime>) bookingDtoOutAbs ->
                ((BookingDtoOut) bookingDtoOutAbs).getStartDate())).hasSameElementsAs(Arrays.asList(
                now.plusDays(4),
                now.plusDays(2)
        ));

    }

    @Test
    void should_getPastCurrentFutureBookingByBooker() {
        BookingDtoOut past = (BookingDtoOut) bookingService.create(BookingDtoIn.builder()
                .itemId(savedItem.getId())
                .startDate(now.minusDays(2))
                .endDate(now.minusDays(1))
                .build(), savedBooker.getId());

        BookingDtoOut current = (BookingDtoOut) bookingService.create(BookingDtoIn.builder()
                .itemId(savedItem.getId())
                .startDate(now.minusDays(1))
                .endDate(now.plusDays(1))
                .build(), savedBooker.getId());

        BookingDtoOut future = (BookingDtoOut) bookingService.create(BookingDtoIn.builder()
                .itemId(savedItem.getId())
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build(), savedBooker.getId());


        List<? extends BookingDtoOutAbs> bookings = bookingService.getPastBookingByBooker(savedBooker.getId(), 0, 10);
        assertThat(bookings.get(0)).isEqualTo(past);
        bookings = bookingService.getCurrentBookingByBooker(savedBooker.getId(), 0, 10);
        assertThat(bookings.get(0)).isEqualTo(current);
        bookings = bookingService.getFutureBookingByBooker(savedBooker.getId(), 0, 10);
        assertThat(bookings.get(0)).isEqualTo(future);
    }

    @Test
    void should_getAllByOwner() {
        for (int i = 1; i < 10; i++) {
            BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
                    .itemId(savedItem.getId())
                    .startDate(now.plusDays(i))
                    .endDate(now.plusDays(i + 1))
                    .build();

            BookingDtoOut booking = (BookingDtoOut) bookingService.create(bookingDtoIn, savedBooker.getId());
        }


        List<? extends BookingDtoOutAbs> allByBooker = bookingService.getAllByOwner(savedOwner.getId(), 3, 2);

        assertThat(allByBooker.stream().map((Function<BookingDtoOutAbs, LocalDateTime>) bookingDtoOutAbs ->
                ((BookingDtoOut) bookingDtoOutAbs).getStartDate())).hasSameElementsAs(Arrays.asList(
                now.plusDays(6),
                now.plusDays(5)
        ));

    }

    @Test
    void should_getBookingByOwnerAndStatus() {
        for (int i = 1; i < 10; i++) {
            BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
                    .itemId(savedItem.getId())
                    .startDate(now.plusDays(i))
                    .endDate(now.plusDays(i + 1))
                    .build();

            BookingDtoOut booking = (BookingDtoOut) bookingService.create(bookingDtoIn, savedBooker.getId());

            if (i % 2 == 0)
                bookingService.approve(booking.getId(), savedOwner.getId(), true);
        }

        List<? extends BookingDtoOutAbs> booking = bookingService.getBookingByOwnerAndStatus(savedOwner.getId(), BookingStatus.APPROVED, 2, 2);

        assertThat(booking.stream().map((Function<BookingDtoOutAbs, LocalDateTime>) bookingDtoOutAbs ->
                ((BookingDtoOut) bookingDtoOutAbs).getStartDate())).hasSameElementsAs(Arrays.asList(
                now.plusDays(4),
                now.plusDays(2)
        ));
    }

    @Test
    void should_getPastCurrentFutureBookingByOwner() {
        BookingDtoOut past = (BookingDtoOut) bookingService.create(BookingDtoIn.builder()
                .itemId(savedItem.getId())
                .startDate(now.minusDays(2))
                .endDate(now.minusDays(1))
                .build(), savedBooker.getId());

        BookingDtoOut current = (BookingDtoOut) bookingService.create(BookingDtoIn.builder()
                .itemId(savedItem.getId())
                .startDate(now.minusDays(1))
                .endDate(now.plusDays(1))
                .build(), savedBooker.getId());

        BookingDtoOut future = (BookingDtoOut) bookingService.create(BookingDtoIn.builder()
                .itemId(savedItem.getId())
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build(), savedBooker.getId());


        List<? extends BookingDtoOutAbs> bookings = bookingService.getPastBookingByOwner(savedOwner.getId(), 0, 10);
        assertThat(bookings.get(0)).isEqualTo(past);
        bookings = bookingService.getCurrentBookingByOwner(savedOwner.getId(), 0, 10);
        assertThat(bookings.get(0)).isEqualTo(current);
        bookings = bookingService.getFutureBookingByOwner(savedOwner.getId(), 0, 10);
        assertThat(bookings.get(0)).isEqualTo(future);
    }

    @Test
    public void should_deleteBooking() {
        BookingDtoOut booking1 = (BookingDtoOut) bookingService.create(BookingDtoIn.builder()
                .itemId(savedItem.getId())
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build(), savedBooker.getId());

        BookingDtoOut booking2 = (BookingDtoOut) bookingService.create(BookingDtoIn.builder()
                .itemId(savedItem.getId())
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build(), savedBooker.getId());


        bookingService.delete(booking1.getId());
        assertThat(bookingService.getAllByOwner(savedOwner.getId(), 0, 10).get(0)).isEqualTo(booking2);
    }

}
