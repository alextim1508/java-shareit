package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.controller.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.Collection;

public interface BookingService {

    Booking create(Booking booking);

    Booking getById(int id, int userId);

    Collection<Booking> getAllByBooker(int userId);

    Collection<Booking> getBookingByOwnerAndStatus(int userId);

    Collection<Booking> getFutureBookingByOwner(int userId);

    Collection<Booking> getBookingByOwnerAndStatus(int userId, BookingStatus status);

    Collection<Booking> getCurrentBookingByOwner(int userId);

    Booking update(int id, BookingDto booking);

    Booking setApproved(int id, int ownerId, boolean approved);

    Collection<Booking> getAllByOwner(int ownerId);

    Collection<Booking> getBookingByBookerAndStatus(int userId);

    Collection<Booking> getFutureBookingByBooker(int userId);

    Collection<Booking> getBookingByBookerAndStatus(int userId, BookingStatus status);

    Collection<Booking> getCurrentBookingByBooker(int userId);

    void delete(int id);
}
