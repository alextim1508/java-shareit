package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.util.exception.UnsupportedStatusException;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.user.controller.UserController.USER_ID_HEADER;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    private final BookingMapper bookingMapper;

    private static final String DEFAULT_PAGE_SIZE = "100";

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookingOutputDto create(@RequestHeader(value = USER_ID_HEADER) int userId,
                                   @Valid @RequestBody BookingInputDto bookingDto,
                                   BindingResult result) {
        if (result.getErrorCount() != 0) {
            log.error("Validation errors: {}", result.getAllErrors());
            throw new ValidationException();
        }

        Booking booking = bookingService.create(bookingMapper.fromDto(bookingDto, userId));
        return bookingMapper.toDto(booking);
    }

    @GetMapping("/{id}")
    public BookingOutputDto getById(@PathVariable("id") int id,
                                    @RequestHeader(value = USER_ID_HEADER) int ownerId) {
        Booking booking = bookingService.getById(id, ownerId);
        return bookingMapper.toDto(booking);
    }

    @GetMapping
    public List<BookingOutputDto> getAllByBooker(@RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
                                                 @RequestHeader(value = USER_ID_HEADER) int userId,
                                                 @Min(0) @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                 @Min(1) @RequestParam(value = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
        List<Booking> bookings;

        switch (state) {
            case "ALL":
                bookings = bookingService.getAllByBooker(userId, from, size);
                break;
            case "CURRENT":
                bookings = bookingService.getCurrentBookingByBooker(userId, from, size);
                break;
            case "PAST":
                bookings = bookingService.getPastBookingByBooker(userId, from, size);
                break;
            case "FUTURE":
                bookings = bookingService.getFutureBookingByBooker(userId, from, size);
                break;
            case "WAITING":
                bookings = bookingService.getBookingByBookerAndStatus(userId, BookingStatus.WAITING, from, size);
                break;
            case "REJECTED":
                bookings = bookingService.getBookingByBookerAndStatus(userId, BookingStatus.REJECTED, from, size);
                break;
            default:
                throw new UnsupportedStatusException(state);
        }

        return bookingMapper.toDto(bookings);
    }

    @GetMapping("/owner")
    public List<BookingOutputDto> getAllByOwner(@RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
                                                @RequestHeader(value = USER_ID_HEADER) int userId,
                                                @Min(0) @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                @Min(1) @RequestParam(value = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
        List<Booking> bookings;

        switch (state) {
            case "ALL":
                bookings = bookingService.getAllByOwner(userId, from, size);
                break;
            case "CURRENT":
                bookings = bookingService.getCurrentBookingByOwner(userId, from, size);
                break;
            case "PAST":
                bookings = bookingService.getPastBookingByOwner(userId, from, size);
                break;
            case "FUTURE":
                bookings = bookingService.getFutureBookingByOwner(userId, from, size);
                break;
            case "WAITING":
                bookings = bookingService.getBookingByOwnerAndStatus(userId, BookingStatus.WAITING, from, size);
                break;
            case "REJECTED":
                bookings = bookingService.getBookingByOwnerAndStatus(userId, BookingStatus.REJECTED, from, size);
                break;
            default:
                throw new UnsupportedStatusException(state);
        }

        return bookingMapper.toDto(bookings);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutputDto setApproved(@PathVariable("bookingId") int id,
                                        @RequestParam("approved") boolean isApproved,
                                        @RequestHeader(value = USER_ID_HEADER) int ownerId) {
        Booking booking = bookingService.approve(id, ownerId, isApproved);

        return bookingMapper.toDto(booking);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        bookingService.delete(id);
    }
}
