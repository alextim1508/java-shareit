package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.controller.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exception.ForbiddenException;
import ru.practicum.shareit.util.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.util.exception.ResourceNotFoundException;
import ru.practicum.shareit.util.exception.StatusChangeException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.model.BookingStatus.REJECTED;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final ItemService itemService;

    private final UserService userService;

    @Override
    public Booking create(Booking booking) {

        userService.getById(booking.getBooker().getId());

        if (!itemService.getById(booking.getItem().getId()).getAvailable())
            throw new ItemIsNotAvailableException();

        Item byId = itemService.getById(booking.getItem().getId());

        if (booking.getBooker().getId().equals(byId.getOwner().getId()))
            throw new ResourceNotFoundException();

        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking setApproved(int id, int ownerId, boolean approved) {
        Booking byId = bookingRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        if (!byId.getItem().getOwner().getId().equals(ownerId))
            throw new ForbiddenException();

        if (byId.getStatus() == APPROVED)
            throw new StatusChangeException();

        if (approved)
            byId.setStatus(APPROVED);
        else
            byId.setStatus(REJECTED);

        return byId;
    }

    @Override
    public Collection<Booking> getAllByBooker(int userId) {
        userService.getById(userId);
        return bookingRepository.getBookingByBooker(userId);
    }

    @Override
    public Collection<Booking> getPastBookingByBooker(int userId) {
        userService.getById(userId);
        return bookingRepository.getPastBookingByBooker(userId);
    }

    @Override
    public Collection<Booking> getFutureBookingByBooker(int userId) {
        userService.getById(userId);
        return bookingRepository.getFutureBookingByBooker(userId);
    }

    @Override
    public Collection<Booking> getBookingByBookerAndStatus(int userId, BookingStatus status) {
        userService.getById(userId);
        return bookingRepository.getBookingByBookerAndStatus(userId, status);
    }

    @Override
    public Collection<Booking> getCurrentBookingByBooker(int userId) {
        userService.getById(userId);
        return bookingRepository.getCurrentBookingByBooker(userId);
    }

    @Override
    public Collection<Booking> getAllByOwner(int userId) {
        userService.getById(userId);
        return bookingRepository.getBookingByOwner(userId);
    }

    @Override
    public Collection<Booking> getPastBookingByOwner(int userId) {
        userService.getById(userId);
        return bookingRepository.getPastBookingByOwner(userId);
    }

    @Override
    public Collection<Booking> getFutureBookingByOwner(int userId) {
        userService.getById(userId);
        return bookingRepository.getFutureBookingByOwner(userId);
    }

    @Override
    public Collection<Booking> getBookingByOwnerAndStatus(int userId, BookingStatus status) {
        userService.getById(userId);
        return bookingRepository.getBookingByOwnerAndStatus(userId, status);
    }

    @Override
    public Collection<Booking> getCurrentBookingByOwner(int userId) {
        userService.getById(userId);
        return bookingRepository.getCurrentBookingByOwner(userId);
    }


    @Override
    public Booking getById(int id, int userId) {
        Booking booking = bookingRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId)
            throw new ForbiddenException();

        return booking;
    }

    @Override
    public Booking update(int id, BookingDto bookingDto) {
        Booking booking = bookingRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        return bookingRepository.save(booking);
    }

    @Override
    public void delete(int id) {
        bookingRepository.deleteById(id);
    }

    public static Booking getLastBooking(Set<Booking> sortedBookings, LocalDateTime now) {
        Booking lastBooking = null;
        for (Booking booking : sortedBookings) {
            if (booking.getStartDate().isBefore(now)) {
                lastBooking = booking;
            }
        }
        return lastBooking;
    }

    public static Booking getNextBooking(Set<Booking> sortedBookings, LocalDateTime now) {
        for (Booking booking : sortedBookings) {
            if (booking.getStartDate().isAfter(now)) {
                return booking;
            }
        }
        return null;
    }
}
