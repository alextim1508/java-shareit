package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;

@Data
@AllArgsConstructor
public class NearestBookingDto {

    private Integer id;

    private Integer bookerId;

    public static NearestBookingDto toNearestBookingDto(Booking booking) {
        return new NearestBookingDto(booking.getId(), booking.getBooker().getId());
    }
}
