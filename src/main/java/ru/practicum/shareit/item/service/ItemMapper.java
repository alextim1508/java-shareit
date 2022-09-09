package ru.practicum.shareit.item.service;

import org.mapstruct.*;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.item.controller.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.service.ItemRequestFactory;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {BookingMapper.class, CommentMapper.class, UserService.class, ItemFactory.class, ItemRequestFactory.class})
public interface ItemMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    void updateItemFromDto(ItemDto itemDto, @MappingTarget Item item);

    List<ItemDto> toDto(List<Item> items);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "itemDto.requestId", target = "request")
    @Mapping(source = "ownerId", target = "owner")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    Item fromDto(ItemDto itemDto, Integer ownerId);

    @Mapping(source = "request.id", target = "requestId")
    ItemDto toDto(Item item);
}