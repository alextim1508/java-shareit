package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.OffsetLimitPageable;
import ru.practicum.shareit.util.exception.ForbiddenException;
import ru.practicum.shareit.util.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.util.exception.NotFoundException;
import ru.practicum.shareit.util.exception.StatusChangeException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.model.BookingStatus.*;
import static ru.practicum.shareit.booking.service.BookingServiceImpl.getLastBooking;
import static ru.practicum.shareit.booking.service.BookingServiceImpl.getNextBooking;

@SpringBootTest
public class BookingServiceTest {

    @MockBean
    BookingRepository bookingRepository;

    @MockBean
    ItemService itemService;

    @MockBean
    UserService userService;

    @Autowired
    BookingService bookingService;


    User owner, booker;
    Item item;
    Booking booking;

    @BeforeEach
    void setUp() {
        doNothing().when(userService).existenceCheck(anyInt());

        LocalDateTime now = LocalDateTime.now();

        owner = new User(2, "owner", "mail@gmail.com");
        item = Item.builder()
                .id(1)
                .name("name")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        booker = new User(1, "booker", "booker@gmail.com");

        booking = Booking.builder()
                .startDate(now.minusDays(2))
                .endDate(now.minusDays(1))
                .item(item)
                .booker(booker)
                .build();
    }

    @Test
    void create_shouldInvokeRepositoryAndReturnTheSame() {
        when(bookingRepository.save(booking)).thenReturn(booking);

        when(itemService.getById(anyInt(), anyInt()))
                .thenReturn(item);

        Booking savedBooking = bookingService.create(booking);

        verify(bookingRepository, times(1)).save(booking);
        assertThat(savedBooking).isEqualTo(booking);
    }

    @Test
    void create_shouldThrowItemIsNotAvailableExceptionWhenItemIsNotAvailable() {
        when(itemService.getById(anyInt(), anyInt()))
                .thenReturn(item);

        item.setAvailable(false);

        assertThatThrownBy(() -> {
            bookingService.create(booking);
        }).isInstanceOf(ItemIsNotAvailableException.class);
    }

    @Test
    void create_shouldThrowNotFoundExceptionWhenOwnerAndBookerAreTheSame() {
        when(itemService.getById(anyInt(), anyInt()))
                .thenReturn(item);

        booking.setBooker(owner);

        assertThatThrownBy(() -> {
            bookingService.create(booking);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getById_shouldThrowNotFoundExceptionWhenRepositoryReturnsEmpty() {
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            bookingService.getById(1, 1);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getById_shouldThrowForbiddenExceptionWhenUserIsNotOwnerOrBooker() {
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(booking));

        final int USER_ID = 3;

        assertThat(USER_ID)
                .isNotEqualTo(owner.getId())
                .isNotEqualTo(booker.getId());

        assertThatThrownBy(() -> {
            bookingService.getById(1, USER_ID);
        }).isInstanceOf(ForbiddenException.class);
    }

    @Test
    void getById_shouldInvokeRepositoryAndReturnTheSameWhenUserIsOwner() {
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(booking));

        final int USER_ID = 2;

        Booking bookingById = bookingService.getById(1, USER_ID);

        assertThat(bookingById).isEqualTo(booking);

        verify(bookingRepository, times(1)).findById(1);
    }

    @Test
    void getById_shouldInvokeRepositoryAndReturnTheSameWhenUserIsBooker() {
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(booking));

        final int USER_ID = 1;
        assertThat(USER_ID).isEqualTo(booker.getId());

        Booking bookingById = bookingService.getById(1, USER_ID);

        verify(bookingRepository, times(1)).findById(1);
        assertThat(bookingById).isEqualTo(booking);
    }

    @Test
    void getAllByBooker_shouldInvokeRepositoryAndReturnTheSame() {
        when(bookingRepository.getBookingByBooker(anyInt(), any(OffsetLimitPageable.class)))
                .thenReturn(Arrays.asList(booking));

        List<Booking> bookings = bookingService.getAllByBooker(1, 0, 10);

        assertThat(bookings)
                .containsAll(Arrays.asList(booking));

        verify(bookingRepository, times(1)).getBookingByBooker(eq(1), any(OffsetLimitPageable.class));
    }

    @Test
    void getBookingByBookerAndStatus_shouldInvokeRepositoryAndReturnTheSame() {

        final int BOOKER_ID = booker.getId();

        when(bookingRepository.getBookingByBookerAndStatus(anyInt(), any(BookingStatus.class), any(OffsetLimitPageable.class)))
                .thenReturn(Arrays.asList(booking));

        List<Booking> bookings = bookingService.getBookingByBookerAndStatus(BOOKER_ID, WAITING, 0, 10);

        assertThat(bookings)
                .containsAll(Arrays.asList(booking));

        verify(bookingRepository, times(1)).getBookingByBookerAndStatus(eq(BOOKER_ID), eq(WAITING), any(OffsetLimitPageable.class));
    }

    @Test
    void getPastBookingByBooker_shouldInvokeRepositoryAndReturnTheSame() {
        final int BOOKER_ID = booker.getId();

        when(bookingRepository.getPastBookingByBooker(anyInt(), any(OffsetLimitPageable.class)))
                .thenReturn(Arrays.asList(booking));

        List<Booking> bookings = bookingService.getPastBookingByBooker(BOOKER_ID, 0, 10);

        verify(bookingRepository, times(1)).getPastBookingByBooker(eq(BOOKER_ID), any(OffsetLimitPageable.class));
        assertThat(bookings).isNotNull().isNotEmpty().containsAll(Arrays.asList(booking));
    }

    @Test
    void getCurrentBookingByBooker_shouldInvokeRepositoryAndReturnTheSame() {

        final int BOOKER_ID = booker.getId();

        when(bookingRepository.getCurrentBookingByBooker(anyInt(), any(OffsetLimitPageable.class)))
                .thenReturn(Arrays.asList(booking));

        List<Booking> bookings = bookingService.getCurrentBookingByBooker(BOOKER_ID, 0, 10);

        verify(bookingRepository, times(1)).getCurrentBookingByBooker(eq(BOOKER_ID), any(OffsetLimitPageable.class));
        assertThat(bookings).isNotNull().isNotEmpty().containsAll(Arrays.asList(booking));
    }

    @Test
    void getFutureBookingByBooker_shouldInvokeRepositoryAndReturnTheSame() {
        final int BOOKER_ID = booker.getId();

        when(bookingRepository.getFutureBookingByBooker(anyInt(), any(OffsetLimitPageable.class)))
                .thenReturn(Arrays.asList(booking));

        List<Booking> bookings = bookingService.getFutureBookingByBooker(BOOKER_ID, 0, 10);

        verify(bookingRepository, times(1)).getFutureBookingByBooker(eq(BOOKER_ID), any(OffsetLimitPageable.class));
        assertThat(bookings).isNotNull().isNotEmpty().containsAll(Arrays.asList(booking));
    }

    @Test
    void getAllByOwner_shouldInvokeRepositoryAndReturnTheSame() {
        final int OWNER_ID = owner.getId();

        when(bookingRepository.getBookingByOwner(anyInt(), any(OffsetLimitPageable.class)))
                .thenReturn(Arrays.asList(booking));

        List<Booking> bookings = bookingService.getAllByOwner(OWNER_ID, 0, 10);

        verify(bookingRepository, times(1)).getBookingByOwner(eq(OWNER_ID), any(OffsetLimitPageable.class));
        assertThat(bookings).isNotNull().isNotEmpty().containsAll(Arrays.asList(booking));
    }

    @Test
    void getBookingByOwnerAndStatus_shouldInvokeRepositoryAndReturnTheSame() {
        final int OWNER_ID = owner.getId();

        when(bookingRepository.getBookingByOwnerAndStatus(anyInt(), any(BookingStatus.class), any(OffsetLimitPageable.class)))
                .thenReturn(Arrays.asList(booking));

        List<Booking> bookings = bookingService.getBookingByOwnerAndStatus(OWNER_ID, WAITING, 0, 10);

        verify(bookingRepository, times(1)).getBookingByOwnerAndStatus(eq(OWNER_ID), eq(WAITING), any(OffsetLimitPageable.class));
        assertThat(bookings).isNotNull().isNotEmpty().containsAll(Arrays.asList(booking));
    }

    @Test
    void getPastBookingByOwner_shouldInvokeRepositoryAndReturnTheSame() {

        final int OWNER_ID = owner.getId();

        when(bookingRepository.getPastBookingByOwner(anyInt(), any(OffsetLimitPageable.class)))
                .thenReturn(Arrays.asList(booking));

        List<Booking> bookings = bookingService.getPastBookingByOwner(OWNER_ID, 0, 10);

        verify(bookingRepository, times(1)).getPastBookingByOwner(eq(OWNER_ID), any(OffsetLimitPageable.class));
        assertThat(bookings).isNotNull().isNotEmpty().containsAll(Arrays.asList(booking));
    }

    @Test
    void getCurrentBookingByOwner_shouldInvokeRepositoryAndReturnTheSame() {


        final int OWNER_ID = owner.getId();


        when(bookingRepository.getCurrentBookingByOwner(anyInt(), any(OffsetLimitPageable.class)))
                .thenReturn(Arrays.asList(booking));

        List<Booking> bookings = bookingService.getCurrentBookingByOwner(OWNER_ID, 0, 10);

        verify(bookingRepository, times(1)).getCurrentBookingByOwner(eq(OWNER_ID), any(OffsetLimitPageable.class));
        assertThat(bookings).isNotNull().isNotEmpty().containsAll(Arrays.asList(booking));
    }

    @Test
    void getFutureBookingByOwner_shouldInvokeRepositoryAndReturnTheSame() {

        final int OWNER_ID = owner.getId();

        when(bookingRepository.getFutureBookingByOwner(anyInt(), any(OffsetLimitPageable.class)))
                .thenReturn(Arrays.asList(booking));

        List<Booking> bookings = bookingService.getFutureBookingByOwner(OWNER_ID, 0, 10);

        verify(bookingRepository, times(1)).getFutureBookingByOwner(eq(OWNER_ID), any(OffsetLimitPageable.class));
        assertThat(bookings).isNotNull().isNotEmpty().containsAll(Arrays.asList(booking));
    }

    @Test
    void approve_shouldThrowForbiddenExceptionWhenUserIsNotOwner() {
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(booking));

        final int OTHER_ID = 3;
        assertThat(OTHER_ID)
                .isNotEqualTo(owner.getId());

        assertThatThrownBy(() -> {
            bookingService.approve(1, booker.getId(), true);
        }).isInstanceOf(ForbiddenException.class);
    }

    @Test
    void approve_shouldThrowForbiddenExceptionWhenStatusInNotWaiting() {
        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(booking));

        booking.setStatus(CANCELED);

        assertThatThrownBy(() -> {
            bookingService.approve(1, owner.getId(), true);
        }).isInstanceOf(StatusChangeException.class);
    }

    @Test
    void approve_shouldThrowForbiddenExceptionWhenStatusIsWaiting() {
        booking.setStatus(WAITING);

        when(bookingRepository.findById(anyInt()))
                .thenReturn(Optional.of(booking));

        bookingService.approve(1, owner.getId(), true);
    }


    @Test
    void delete_shouldInvokeRepositoryDelete() {
        doNothing().when(bookingRepository)
                .deleteById(anyInt());

        bookingService.delete(1);
        verify(bookingRepository, times(1)).deleteById(1);
    }

    @Test
    void delete_shouldThrowNotFoundExceptionWhenInvokeRepositoryWithWrongId() {
        doThrow(EmptyResultDataAccessException.class).when(bookingRepository)
                .deleteById(anyInt());

        assertThatThrownBy(() -> bookingService.delete(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Booking with ID 1 is not found");
    }

    @Test
    void getLastBooking_shouldReturnLastBooking() {
        LocalDateTime now = LocalDateTime.now();

        Booking beforeOneLastBooking = Booking.builder()
                .startDate(now.minusMinutes(3))
                .endDate(now.minusMinutes(2))
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();

        Booking lastBooking = Booking.builder()
                .startDate(now.minusMinutes(2))
                .endDate(now.minusMinutes(1))
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();

        Booking currentBooking = Booking.builder()
                .startDate(now.minusMinutes(1))
                .endDate(now.plusMinutes(1))
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();
        item.setBookings(Arrays.asList(currentBooking, lastBooking, beforeOneLastBooking));

        assertThat(getLastBooking(item, LocalDateTime.now())).isEqualTo(lastBooking);
    }

    @Test
    void getNextBooking_shouldReturnNextBooking() {
        LocalDateTime now = LocalDateTime.now();

        Booking afterOneNextBooking = Booking.builder()
                .startDate(now.plusMinutes(2))
                .endDate(now.plusMinutes(3))
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();

        Booking nextBooking = Booking.builder()
                .startDate(now.plusMinutes(1))
                .endDate(now.plusMinutes(2))
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();


        Booking currentBooking = Booking.builder()
                .startDate(now.minusMinutes(1))
                .endDate(now.plusMinutes(1))
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();
        item.setBookings(Arrays.asList(currentBooking, nextBooking, afterOneNextBooking));

        assertThat(getNextBooking(item, LocalDateTime.now())).isEqualTo(nextBooking);
    }
}