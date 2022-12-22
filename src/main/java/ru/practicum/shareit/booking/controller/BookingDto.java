package ru.practicum.shareit.booking.controller;


import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.controller.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.validator.EarlierThan;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.shareit.item.controller.ItemDto.toItemDto;
import static ru.practicum.shareit.user.controller.UserDto.toUserDto;

@EarlierThan(value = "start", earlierThan = "end")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private Integer id;

    @NotNull
    @NonNull
    private Integer itemId;

    @Future
    @NotNull
    @NonNull
    private LocalDateTime start;

    @Future
    @NotNull
    @NonNull
    private LocalDateTime end;

    private String status;

    private UserDto booker;

    private ItemDto item;

    public static BookingDto toBookingDto(Booking booking, User booker, Item item) {
        BookingDto bookingDto = toBookingDto(booking, booker);
        bookingDto.setItem(toItemDto(item));
        return bookingDto;
    }

    public static BookingDto toBookingDto(Booking booking, User booker) {
        BookingDto bookingDto = toBookingDto(booking);
        bookingDto.setBooker(toUserDto(booker));
        return bookingDto;
    }

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto(booking.getItem().getId(),
                booking.getStartDate(),
                booking.getEndDate());
        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus().name());
        return bookingDto;
    }

    public static Booking toBooking(BookingDto dto, int bookerId) {
        return new Booking(
                dto.getStart(),
                dto.getEnd(),
                new Item(dto.getItemId()),
                new User(bookerId),
                BookingStatus.WAITING);
    }
}
