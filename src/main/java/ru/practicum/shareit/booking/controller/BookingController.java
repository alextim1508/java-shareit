package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.util.exception.UnsupportedStatusException;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.controller.BookingDto.toBooking;
import static ru.practicum.shareit.booking.controller.BookingDto.toBookingDto;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                              @Valid @RequestBody BookingDto bookingDto,
                              BindingResult result) {
        if (result.getErrorCount() != 0) {
            log.error("Validation errors: {}", result.getAllErrors());
            throw new ValidationException();
        }

        Booking booking = bookingService.create(toBooking(bookingDto, userId));

        return toBookingDto(booking);
    }


    @PatchMapping("/{bookingId}")
    public BookingDto setApproved(@PathVariable("bookingId") int id,
                                   @RequestParam("approved") boolean isApproved,
                                   @RequestHeader(value = "X-Sharer-User-Id") int ownerId) {
        Booking booking = bookingService.setApproved(id, ownerId, isApproved);

        return toBookingDto(booking, booking.getBooker(), booking.getItem());
    }


    @GetMapping("/{id}")
    public BookingDto getById(@PathVariable("id") int id,
                               @RequestHeader(value = "X-Sharer-User-Id") int ownerId) {
        Booking byId = bookingService.getById(id, ownerId);

        return toBookingDto(byId, byId.getBooker(), byId.getItem());
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(@RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
                                            @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        Collection<Booking> bookings;

        switch (state) {
            case "ALL":
                bookings = bookingService.getAllByBooker(userId);
                break;
            case "CURRENT":
                bookings = bookingService.getCurrentBookingByBooker(userId);
                break;
            case "PAST":
                bookings = bookingService.getPastBookingByBooker(userId);
                break;
            case "FUTURE":
                bookings = bookingService.getFutureBookingByBooker(userId);
                break;
            case "WAITING":
                bookings = bookingService.getBookingByBookerAndStatus(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingService.getBookingByBookerAndStatus(userId, BookingStatus.REJECTED);
                break;
            default:
                throw new UnsupportedStatusException(state);
        }

        return bookings.stream()
                .map(booking -> toBookingDto(booking, booking.getBooker(), booking.getItem()))
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
                                           @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        Collection<Booking> bookings;

        switch (state) {
            case "ALL":
                bookings = bookingService.getAllByOwner(userId);
                break;
            case "CURRENT":
                bookings = bookingService.getCurrentBookingByOwner(userId);
                break;
            case "PAST":
                bookings = bookingService.getPastBookingByOwner(userId);
                break;
            case "FUTURE":
                bookings = bookingService.getFutureBookingByOwner(userId);
                break;
            case "WAITING":
                bookings = bookingService.getBookingByOwnerAndStatus(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingService.getBookingByOwnerAndStatus(userId, BookingStatus.REJECTED);
                break;

            default:
                throw new UnsupportedStatusException(state);
        }

        return bookings.stream()
                .map(booking -> toBookingDto(booking, booking.getBooker(), booking.getItem()))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        bookingService.delete(id);
    }
}
