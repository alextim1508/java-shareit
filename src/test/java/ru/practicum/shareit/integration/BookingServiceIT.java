package ru.practicum.shareit.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exception.ForbiddenException;
import ru.practicum.shareit.util.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    @Test
    void should_saveBooking() {
        User owner = new User("owner", "owner@gmail.com");
        userService.create(owner);

        Item item = new Item("item", "description", true, owner);
        itemService.create(item);

        User booker = new User("booker", "booker@gmail.com");
        userService.create(booker);

        Booking booking = new Booking(
                LocalDateTime.of(2022, Month.SEPTEMBER, 1, 12, 0),
                LocalDateTime.of(2022, Month.SEPTEMBER, 2, 12, 0),
                item,
                booker);

        bookingService.create(booking);
    }

    @Test
    void should_throwException_when_savingItemIsNotAvailable() {
        User owner = new User("owner", "owner@gmail.com");
        userService.create(owner);

        Item item = new Item("item", "description", false, owner);
        itemService.create(item);

        User booker = new User("booker", "booker@gmail.com");
        userService.create(booker);

        Booking booking = new Booking(
                LocalDateTime.of(2022, Month.SEPTEMBER, 1, 12, 0),
                LocalDateTime.of(2022, Month.SEPTEMBER, 2, 12, 0),
                item,
                booker);

        assertThatThrownBy(() -> {
            bookingService.create(booking);
        }).isInstanceOf(ItemIsNotAvailableException.class);
    }

    @Test
    void should_throwException_when_ownerAndBookerAreTheSame() {
        User owner = new User("owner", "owner@gmail.com");
        userService.create(owner);

        Item item = new Item("item", "description", true, owner);
        itemService.create(item);

        Booking booking = new Booking(
                LocalDateTime.of(2022, Month.SEPTEMBER, 1, 12, 0),
                LocalDateTime.of(2022, Month.SEPTEMBER, 2, 12, 0),
                item,
                owner);

        assertThatThrownBy(() -> {
            bookingService.create(booking);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    void should_throwException_when_bookingIsMissingInDB() {
        assertThatThrownBy(() -> {
            bookingService.getById(1, 1);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    void should_throwException_when_userIsNotOwnerOrBooker() {
        User owner = new User("owner", "owner@gmail.com");
        userService.create(owner);

        Item item = new Item("item", "description", true, owner);
        itemService.create(item);

        User booker = new User("booker", "booker@gmail.com");
        userService.create(booker);

        User user = new User("user", "user@gmail.com");
        userService.create(user);

        Booking booking = new Booking(
                LocalDateTime.of(2022, Month.SEPTEMBER, 1, 12, 0),
                LocalDateTime.of(2022, Month.SEPTEMBER, 2, 12, 0),
                item,
                booker);

        bookingService.create(booking);


        assertThatThrownBy(() -> {
            bookingService.getById(booking.getId(), user.getId());
        }).isInstanceOf(ForbiddenException.class);
    }

    @Test
    void should_getById() {
        User owner = new User("owner", "owner@gmail.com");
        userService.create(owner);

        Item item = new Item("item", "description", true, owner);
        itemService.create(item);

        User booker = new User("booker", "booker@gmail.com");
        userService.create(booker);

        Booking booking = new Booking(
                LocalDateTime.of(2022, Month.SEPTEMBER, 1, 12, 0),
                LocalDateTime.of(2022, Month.SEPTEMBER, 2, 12, 0),
                item,
                booker);

        bookingService.create(booking);

        assertThat(bookingService.getById(booking.getId(), owner.getId())).isEqualTo(booking);
        assertThat(bookingService.getById(booking.getId(), booker.getId())).isEqualTo(booking);
    }

    @Test
    void should_getAllByBooker() {
        User owner = new User("owner", "owner@gmail.com");
        userService.create(owner);

        Item item = new Item("item", "description", true, owner);
        itemService.create(item);

        User booker = new User("booker", "booker@gmail.com");
        userService.create(booker);

        for (int i = 1; i < 10; i++) {
            Booking booking = new Booking(
                    LocalDateTime.of(2022, Month.SEPTEMBER, i, 12, 0),
                    LocalDateTime.of(2022, Month.SEPTEMBER, i + 1, 12, 0),
                    item,
                    booker);
            bookingService.create(booking);
        }

        List<Booking> allByBooker = bookingService.getAllByBooker(booker.getId(), 3, 2);

        assertThat(allByBooker.stream().map(Booking::getStartDate)).hasSameElementsAs(Arrays.asList(
                LocalDateTime.of(2022, Month.SEPTEMBER, 6, 12, 0),
                LocalDateTime.of(2022, Month.SEPTEMBER, 5, 12, 0)
        ));
    }

    @Test
    void should_getBookingByBookerAndStatus() {
        User owner = new User("owner", "owner@gmail.com");
        userService.create(owner);

        Item item = new Item("item", "description", true, owner);
        itemService.create(item);

        User booker = new User("booker", "booker@gmail.com");
        userService.create(booker);

        for (int i = 1; i < 10; i++) {
            Booking booking = new Booking(
                    LocalDateTime.of(2022, Month.SEPTEMBER, i, 12, 0),
                    LocalDateTime.of(2022, Month.SEPTEMBER, i + 1, 12, 0),
                    item,
                    booker);
            bookingService.create(booking);

            if (i % 2 == 0)
                bookingService.approve(booking.getId(), owner.getId(), true);
        }

        List<Booking> allByBooker = bookingService.getBookingByBookerAndStatus(booker.getId(), BookingStatus.APPROVED, 2, 2);

        assertThat(allByBooker.stream().map(Booking::getStartDate)).hasSameElementsAs(Arrays.asList(
                LocalDateTime.of(2022, Month.SEPTEMBER, 4, 12, 0),
                LocalDateTime.of(2022, Month.SEPTEMBER, 2, 12, 0)
        ));
    }

    @Test
    void should_getPastCurrentFutureBookingByBooker() {
        User owner = new User("owner", "owner@gmail.com");
        userService.create(owner);

        Item item = new Item("item", "description", true, owner);
        itemService.create(item);

        User booker = new User("booker", "booker@gmail.com");
        userService.create(booker);

        LocalDateTime now = LocalDateTime.now();

        Booking pastBooking = new Booking(
                now.minusDays(2),
                now.minusDays(1),
                item,
                booker);
        bookingService.create(pastBooking);

        Booking currentBooking = new Booking(
                now.minusHours(1),
                now.plusHours(1),
                item,
                booker);
        bookingService.create(currentBooking);

        Booking futureBooking = new Booking(
                now.plusDays(1),
                now.plusDays(2),
                item,
                booker);
        bookingService.create(futureBooking);

        List<Booking> allByBooker = bookingService.getPastBookingByBooker(booker.getId(), 0, 10);
        assertThat(allByBooker).containsAll(Collections.singleton(pastBooking));
        allByBooker = bookingService.getCurrentBookingByBooker(booker.getId(), 0, 10);
        assertThat(allByBooker).containsAll(Collections.singleton(currentBooking));
        allByBooker = bookingService.getFutureBookingByBooker(booker.getId(), 0, 10);
        assertThat(allByBooker).containsAll(Collections.singleton(futureBooking));
    }

    @Test
    void should_getAllByOwner() {
        User owner = new User("owner", "owner@gmail.com");
        userService.create(owner);

        Item item = new Item("item", "description", true, owner);
        itemService.create(item);

        User booker = new User("booker", "booker@gmail.com");
        userService.create(booker);

        for (int i = 1; i < 10; i++) {
            Booking booking = new Booking(
                    LocalDateTime.of(2022, Month.SEPTEMBER, i, 12, 0),
                    LocalDateTime.of(2022, Month.SEPTEMBER, i + 1, 12, 0),
                    item,
                    booker);
            bookingService.create(booking);
        }

        List<Booking> allByBooker = bookingService.getAllByOwner(owner.getId(), 3, 2);

        assertThat(allByBooker.stream().map(Booking::getStartDate)).hasSameElementsAs(Arrays.asList(
                LocalDateTime.of(2022, Month.SEPTEMBER, 6, 12, 0),
                LocalDateTime.of(2022, Month.SEPTEMBER, 5, 12, 0)
        ));
    }

    @Test
    void should_getBookingByOwnerAndStatus() {
        User owner = new User("owner", "owner@gmail.com");
        userService.create(owner);

        Item item = new Item("item", "description", true, owner);
        itemService.create(item);

        User booker = new User("booker", "booker@gmail.com");
        userService.create(booker);

        for (int i = 1; i < 10; i++) {
            Booking booking = new Booking(
                    LocalDateTime.of(2022, Month.SEPTEMBER, i, 12, 0),
                    LocalDateTime.of(2022, Month.SEPTEMBER, i + 1, 12, 0),
                    item,
                    booker);
            bookingService.create(booking);

            if (i % 2 == 0)
                bookingService.approve(booking.getId(), owner.getId(), true);
        }

        List<Booking> allByBooker = bookingService.getBookingByOwnerAndStatus(owner.getId(), BookingStatus.APPROVED, 2, 2);

        assertThat(allByBooker.stream().map(Booking::getStartDate)).hasSameElementsAs(Arrays.asList(
                LocalDateTime.of(2022, Month.SEPTEMBER, 4, 12, 0),
                LocalDateTime.of(2022, Month.SEPTEMBER, 2, 12, 0)
        ));
    }

    @Test
    void should_getPastCurrentFutureBookingByOwner() {
        User owner = new User("owner", "owner@gmail.com");
        userService.create(owner);

        Item item = new Item("item", "description", true, owner);
        itemService.create(item);

        User booker = new User("booker", "booker@gmail.com");
        userService.create(booker);

        LocalDateTime now = LocalDateTime.now();

        Booking pastBooking = new Booking(
                now.minusDays(2),
                now.minusDays(1),
                item,
                booker);
        bookingService.create(pastBooking);

        Booking currentBooking = new Booking(
                now.minusHours(1),
                now.plusHours(1),
                item,
                booker);
        bookingService.create(currentBooking);

        Booking futureBooking = new Booking(
                now.plusDays(1),
                now.plusDays(2),
                item,
                booker);
        bookingService.create(futureBooking);

        List<Booking> allByBooker = bookingService.getPastBookingByOwner(owner.getId(), 0, 10);
        assertThat(allByBooker).containsAll(Collections.singleton(pastBooking));
        allByBooker = bookingService.getCurrentBookingByOwner(owner.getId(), 0, 10);
        assertThat(allByBooker).containsAll(Collections.singleton(currentBooking));
        allByBooker = bookingService.getFutureBookingByOwner(owner.getId(), 0, 10);
        assertThat(allByBooker).containsAll(Collections.singleton(futureBooking));
    }

    @Test
    public void should_deleteBooking() {
        User owner = new User("owner", "owner@gmail.com");
        userService.create(owner);

        Item item = new Item("item", "description", true, owner);
        itemService.create(item);

        User booker = new User("booker", "booker@gmail.com");
        userService.create(booker);

        Booking booking1 = new Booking(
                LocalDateTime.of(2022, Month.SEPTEMBER, 1, 12, 0),
                LocalDateTime.of(2022, Month.SEPTEMBER, 2, 12, 0),
                item,
                booker);
        bookingService.create(booking1);

        Booking booking2 = new Booking(
                LocalDateTime.of(2022, Month.SEPTEMBER, 3, 12, 0),
                LocalDateTime.of(2022, Month.SEPTEMBER, 4, 12, 0),
                item,
                booker);
        bookingService.create(booking2);

        bookingService.delete(booking1.getId());
        assertThat(bookingService.getAllByOwner(owner.getId(), 0, 10)).hasSize(1).containsAll(Collections.singleton(booking2));
    }
}
