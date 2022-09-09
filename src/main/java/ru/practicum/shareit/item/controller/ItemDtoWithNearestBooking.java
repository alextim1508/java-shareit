package ru.practicum.shareit.item.controller;


import lombok.Getter;
import lombok.ToString;
import ru.practicum.shareit.booking.controller.NearestBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.controller.NearestBookingDto.toNearestBookingDto;


@ToString
public class ItemDtoWithNearestBooking extends ItemDto {

    @Getter
    private NearestBookingDto nextBooking;

    @Getter
    private NearestBookingDto lastBooking;

    @Getter
    private List<CommentDto> comments;

    public ItemDtoWithNearestBooking(Item item,
                                     Booking lastBooking,
                                     Booking nextBooking) {
        super(  item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null);
        if(lastBooking != null)
            this.lastBooking = toNearestBookingDto(lastBooking);
        if(nextBooking != null)
            this.nextBooking = toNearestBookingDto(nextBooking);
        if(item.getComments() != null && !item.getComments().isEmpty())
            this.comments = item.getComments().stream().map(CommentDto::toCommentDto).collect(Collectors.toList());
        else
            this.comments = Collections.emptyList();
    }


}
