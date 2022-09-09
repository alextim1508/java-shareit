package ru.practicum.shareit.booking.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.controller.BookingInputDto;
import ru.practicum.shareit.booking.controller.BookingOutputDto;
import ru.practicum.shareit.booking.controller.ShortBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.service.CommentMapper;
import ru.practicum.shareit.item.service.ItemFactory;
import ru.practicum.shareit.user.service.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {UserService.class, UserMapper.class, ItemFactory.class, CommentMapper.class}
)
public interface BookingMapper {

    @Mapping(source = "bookingDto.itemId", target = "item")
    @Mapping(source = "userId", target = "booker")
    Booking fromDto(BookingInputDto bookingDto, Integer userId);

    BookingOutputDto toDto(Booking booking);

    List<BookingOutputDto> toDto(List<Booking> bookings);

    @Mapping(source = "booker.id", target = "bookerId")
    ShortBookingDto toShortDto(Booking booking);
}
