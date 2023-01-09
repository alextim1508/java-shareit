package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOutAbs;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.util.exception.UnsupportedStateException;

import java.util.List;

import static ru.practicum.shareit.util.Util.USER_ID_HEADER;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOutAbs create(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @RequestBody BookingDtoIn bookingDto) {

        return bookingService.create(bookingDto, userId);
    }

    @GetMapping("/{id}")
    public BookingDtoOutAbs getById(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @PathVariable Integer id) {

        return bookingService.getById(id, userId);
    }

    @GetMapping
    public List<? extends BookingDtoOutAbs> getAllByBooker(
            @RequestHeader(value = USER_ID_HEADER) Integer userId,
            @RequestParam String state,
            @RequestParam Integer from,
            @RequestParam Integer size) {

        switch (state) {
            case "ALL":
                return bookingService.getAllByBooker(userId, from, size);
            case "CURRENT":
                return bookingService.getCurrentBookingByBooker(userId, from, size);
            case "PAST":
                return bookingService.getPastBookingByBooker(userId, from, size);
            case "FUTURE":
                return bookingService.getFutureBookingByBooker(userId, from, size);
            case "WAITING":
                return bookingService.getBookingByBookerAndStatus(userId, BookingStatus.WAITING, from, size);
            case "REJECTED":
                return bookingService.getBookingByBookerAndStatus(userId, BookingStatus.REJECTED, from, size);
            default:
                throw new UnsupportedStateException(state);
        }
    }

    @GetMapping("/owner")
    public List<? extends BookingDtoOutAbs> getAllByOwner(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @RequestParam String state,
            @RequestParam Integer from,
            @RequestParam Integer size) {

        switch (state) {
            case "ALL":
                return bookingService.getAllByOwner(userId, from, size);
            case "CURRENT":
                return bookingService.getCurrentBookingByOwner(userId, from, size);
            case "PAST":
                return bookingService.getPastBookingByOwner(userId, from, size);
            case "FUTURE":
                return bookingService.getFutureBookingByOwner(userId, from, size);
            case "WAITING":
                return bookingService.getBookingByOwnerAndStatus(userId, BookingStatus.WAITING, from, size);
            case "REJECTED":
                return bookingService.getBookingByOwnerAndStatus(userId, BookingStatus.REJECTED, from, size);
            default:
                throw new UnsupportedStateException(state);
        }
    }

    @PatchMapping("/{id}")
    public BookingDtoOutAbs approve(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @PathVariable Integer id,
            @RequestParam Boolean approved) {

        return bookingService.approve(id, userId, approved);
    }
}
