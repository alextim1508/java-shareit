package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingService {

    Booking create(Booking booking);

    Booking getById(int id, int userId);

    List<Booking> getAllByBooker(int userId, int from, int size);

    List<Booking> getBookingByOwnerAndStatus(int userId, BookingStatus status, int from, int size);

    List<Booking> getPastBookingByOwner(int userId, int from, int size);

    List<Booking> getCurrentBookingByOwner(int userId, int from, int size);

    List<Booking> getFutureBookingByOwner(int userId, int from, int size);

    List<Booking> getAllByOwner(int ownerId, int from, int size);

    List<Booking> getBookingByBookerAndStatus(int userId, BookingStatus status, int from, int size);

    List<Booking> getPastBookingByBooker(int userId, int from, int size);

    List<Booking> getCurrentBookingByBooker(int userId, int from, int size);

    List<Booking> getFutureBookingByBooker(int userId, int from, int size);

    Booking approve(int id, int ownerId, boolean approved);

    void delete(int id);
}
