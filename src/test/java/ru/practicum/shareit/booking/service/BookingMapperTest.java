package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.controller.BookingInputDto;
import ru.practicum.shareit.booking.controller.BookingOutputDto;
import ru.practicum.shareit.booking.controller.ShortBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.controller.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemFactory;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;

@SpringBootTest
public class BookingMapperTest {

    @Autowired
    BookingMapper mapper;

    @MockBean
    ItemFactory itemFactory;

    @MockBean
    UserService userService;

    User owner, booker;
    Item item;
    Booking booking;
    UserDto bookerDto;
    ItemDto itemDto;
    BookingOutputDto bookingOutputDto;
    BookingInputDto bookingInputDto;
    ShortBookingDto shortBookingDto;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        owner = new User(1, "owner", "owner@gmail.com");
        item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        booker = new User(1, "booker", "booker@gmail.com");
        booking = Booking.builder()
                .id(1)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .booker(booker)
                .item(item)
                .status(WAITING)
                .build();

        bookerDto = new UserDto(1, "booker", "booker@gmail.com");
        itemDto = ItemDto.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .comments(Collections.emptyList())
                .build();

        bookingOutputDto = BookingOutputDto.builder()
                .id(1)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .item(itemDto)
                .booker(bookerDto)
                .status(WAITING)
                .build();

        bookingInputDto = new BookingInputDto(1, now.plusDays(1), now.plusDays(2));
        shortBookingDto = new ShortBookingDto(1, 1, now.plusDays(1), now.plusDays(2));
    }

    @Test
    void toDto_shouldInvokeServiceAndReturnTheSame() {
        assertThat(mapper.toDto(booking)).isEqualTo(bookingOutputDto);
    }

    @Test
    void toDto_shouldInvokeServiceAndReturnNull() {
        Booking booking = null;
        assertThat(mapper.toDto(booking)).isNull();
    }


    @Test
    void toDtoList_shouldInvokeServiceAndReturnTheSame() {
        assertThat(mapper.toDto(List.of(booking))).isEqualTo(List.of(bookingOutputDto));
    }

    @Test
    void toDtoList_shouldInvokeServiceAndReturnNull() {
        List<Booking> booking = null;
        assertThat(mapper.toDto(booking)).isNull();
    }

    @Test
    void fromDto_shouldInvokeServiceAndReturnTheSame() {
        when(itemFactory.get(anyInt())).thenReturn(item);

        when(userService.getById(anyInt())).thenReturn(booker);
        booking.setStatus(null);
        assertThat(mapper.fromDto(bookingInputDto, 1)).isEqualTo(booking);
    }

    @Test
    void fromDto_shouldInvokeServiceAndReturnNull() {
        BookingInputDto bookingInputDto = null;
        assertThat(mapper.fromDto(bookingInputDto, null)).isNull();
    }

    @Test
    void toShortDto_shouldInvokeServiceAndReturnTheSame() {
        assertThat(mapper.toShortDto(booking)).isEqualTo(shortBookingDto);
    }
}
