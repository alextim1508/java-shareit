package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.OffsetLimitPageable;
import ru.practicum.shareit.util.exception.ForbiddenException;
import ru.practicum.shareit.util.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.util.exception.NotFoundException;
import ru.practicum.shareit.util.exception.StatusChangeException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final UserService userService;

    @Override
    public Booking create(Booking booking) {
        booking.setStatus(WAITING);

        Item itemById = booking.getItem();

        if (!itemById.getAvailable()) {
            log.warn("{} of {} is not available", itemById, booking);
            throw new ItemIsNotAvailableException();
        }

        if (booking.getBooker().getId().equals(itemById.getOwner().getId())) {
            log.warn("The booker can not be the owner: {}", booking);
            throw new NotFoundException(Booking.class);
        }

        Booking savedBooking = bookingRepository.save(booking);
        log.info("{} is saved", savedBooking);
        return savedBooking;
    }

    @Override
    public Booking getById(int id, int userId) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            log.warn("The booking with ID {} is not found", id);
            throw new NotFoundException("The booking with ID " + id + " is not found");
        }

        if (booking.get().getBooker().getId() != userId && booking.get().getItem().getOwner().getId() != userId) {
            log.warn("Access of user with ID {} to {} is prohibited. " +
                    "Only the booker or the owner can get the book", id, booking);
            throw new ForbiddenException();
        }

        log.info("{} is found", booking.get());
        return booking.get();
    }

    @Override
    public List<Booking> getAllByBooker(int bookerId, int from, int size) {
        userService.existenceCheck(bookerId);
        Pageable pageable = new OffsetLimitPageable(from, size, Sort.by(Sort.Direction.DESC, "startDate"));
        List<Booking> bookings = bookingRepository.getBookingByBooker(bookerId, pageable);
        log.info("Found {} booking by the booker with ID {}. From {}, size {}", bookings.size(), bookerId, from, size);
        return bookings;
    }

    @Override
    public List<Booking> getBookingByBookerAndStatus(int bookerId, BookingStatus status, int from, int size) {
        userService.existenceCheck(bookerId);
        Pageable pageable = new OffsetLimitPageable(from, size, Sort.by(Sort.Direction.DESC, "startDate"));
        List<Booking> bookings = bookingRepository.getBookingByBookerAndStatus(bookerId, status, pageable);
        log.info("Found {} booking by the booker with ID {} and {}. From {}, size {}", bookings.size(), bookerId, status, from, size);
        return bookings;
    }

    @Override
    public List<Booking> getPastBookingByBooker(int bookerId, int from, int size) {
        userService.existenceCheck(bookerId);
        Pageable pageable = new OffsetLimitPageable(from, size, Sort.by(Sort.Direction.DESC, "startDate"));
        List<Booking> bookings = bookingRepository.getPastBookingByBooker(bookerId, pageable);
        log.info("Found {} booking by the booker with ID {}. From {}, size {}", bookings.size(), bookerId, from, size);
        return bookings;
    }

    @Override
    public List<Booking> getCurrentBookingByBooker(int bookerId, int from, int size) {
        userService.existenceCheck(bookerId);
        Pageable pageable = new OffsetLimitPageable(from, size, Sort.by(Sort.Direction.DESC, "startDate"));
        List<Booking> bookings = bookingRepository.getCurrentBookingByBooker(bookerId, pageable);
        log.info("Found {} booking by the booker with ID {}. From {}, size {}", bookings.size(), bookerId, from, size);
        return bookings;
    }

    @Override
    public List<Booking> getFutureBookingByBooker(int bookerId, int from, int size) {
        userService.existenceCheck(bookerId);
        Pageable pageable = new OffsetLimitPageable(from, size, Sort.by(Sort.Direction.DESC, "startDate"));
        List<Booking> bookings = bookingRepository.getFutureBookingByBooker(bookerId, pageable);
        log.info("Found {} booking by the booker with ID {}. From {}, size {}", bookings.size(), bookerId, from, size);
        return bookings;
    }

    @Override
    public List<Booking> getAllByOwner(int ownerId, int from, int size) {
        userService.existenceCheck(ownerId);
        Pageable pageable = new OffsetLimitPageable(from, size, Sort.by(Sort.Direction.DESC, "startDate"));
        List<Booking> bookings = bookingRepository.getBookingByOwner(ownerId, pageable);
        log.info("Found {} booking by the owner with ID {}. From {}, size {}", bookings.size(), ownerId, from, size);
        return bookings;
    }

    @Override
    public List<Booking> getBookingByOwnerAndStatus(int ownerId, BookingStatus status, int from, int size) {
        userService.existenceCheck(ownerId);
        Pageable pageable = new OffsetLimitPageable(from, size, Sort.by(Sort.Direction.DESC, "startDate"));
        List<Booking> bookings = bookingRepository.getBookingByOwnerAndStatus(ownerId, status, pageable);
        log.info("Found {} booking by the owner with ID {} and {}. From {}, size {}", bookings.size(), ownerId, status, from, size);
        return bookings;
    }

    @Override
    public List<Booking> getPastBookingByOwner(int ownerId, int from, int size) {
        userService.existenceCheck(ownerId);
        Pageable pageable = new OffsetLimitPageable(from, size, Sort.by(Sort.Direction.DESC, "startDate"));
        List<Booking> bookings = bookingRepository.getPastBookingByOwner(ownerId, pageable);
        log.info("Found {} booking by the owner with ID {}. From {}, size {}", bookings.size(), ownerId, from, size);
        return bookings;
    }

    @Override
    public List<Booking> getCurrentBookingByOwner(int ownerId, int from, int size) {
        userService.existenceCheck(ownerId);
        Pageable pageable = new OffsetLimitPageable(from, size, Sort.by(Sort.Direction.DESC, "startDate"));
        List<Booking> bookings = bookingRepository.getCurrentBookingByOwner(ownerId, pageable);
        log.info("Found {} booking by the owner with ID {}. From {}, size {}", bookings.size(), ownerId, from, size);
        return bookings;
    }

    @Override
    public List<Booking> getFutureBookingByOwner(int ownerId, int from, int size) {
        userService.existenceCheck(ownerId);
        Pageable pageable = new OffsetLimitPageable(from, size, Sort.by(Sort.Direction.DESC, "startDate"));
        List<Booking> bookings = bookingRepository.getFutureBookingByOwner(ownerId, pageable);
        log.info("Found {} booking by the owner with ID {}. From {}, size {}", bookings.size(), ownerId, from, size);
        return bookings;
    }

    @Transactional
    @Override
    public Booking approve(int id, int ownerId, boolean approved) {
        Booking booking = getById(id, ownerId);

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            log.info("User with ID {} can not change status of {}. Only the owner can do it", ownerId, booking);
            throw new ForbiddenException();
        }

        if (booking.getStatus() != WAITING) {
            log.info("The status of {} should not be WAITING", booking);
            throw new StatusChangeException();
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        log.info("The status of {} is updated", booking);

        return booking;
    }

    @Override
    public void delete(int id) {
        try {
            bookingRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Booking with ID " + id + " is not found");
        }
        log.info("Booking ID {} is removed", id);
    }

    public static Booking getLastBooking(Item item, LocalDateTime now) {
        return item.getBookings().stream()
                .sorted(Comparator.comparing(Booking::getEndDate).reversed())
                .filter(booking -> now.isAfter(booking.getEndDate()))
                .findFirst()
                .orElse(null);
    }

    public static Booking getNextBooking(Item item, LocalDateTime now) {
        return item.getBookings().stream()
                .sorted(Comparator.comparing(Booking::getStartDate))
                .filter(booking -> now.isBefore(booking.getStartDate()))
                .findFirst()
                .orElse(null);
    }
}
