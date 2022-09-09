package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.validator.NullAllowed;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.service.BookingServiceImpl.getLastBooking;
import static ru.practicum.shareit.booking.service.BookingServiceImpl.getNextBooking;
import static ru.practicum.shareit.item.controller.CommentDto.toComment;
import static ru.practicum.shareit.item.controller.CommentDto.toCommentDto;
import static ru.practicum.shareit.item.controller.ItemDto.toItem;
import static ru.practicum.shareit.item.controller.ItemDto.toItemDto;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    private ItemDto create(@RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                           @Valid @RequestBody ItemDto itemDto,
                           BindingResult result) {
        if (result.getErrorCount() != 0) {
            log.error("Validation errors: {}", result.getAllErrors());
            throw new ValidationException();
        }

        return toItemDto(itemService.create(toItem(itemDto, ownerId)));
    }

    @GetMapping("/{id}")
    private ItemDtoWithNearestBooking getById(@RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                                              @PathVariable("id") int id) {

        LocalDateTime now = LocalDateTime.now();

        Item byId = itemService.getById(id);

        Set<Booking> bookings = byId.getBookings();

        Booking lastBooking = null;
        Booking nextBooking = null;

        if(bookings!= null && !bookings.isEmpty() && byId.getOwner().getId().equals(ownerId)) {
            lastBooking = getLastBooking(bookings, now);
            nextBooking = getNextBooking(bookings, now);
        }

        return new ItemDtoWithNearestBooking(byId, lastBooking, nextBooking);
    }

    @GetMapping("/search")
    private List<ItemDto> search(@RequestParam("text") String pattern) {
        return itemService.getAll(pattern, true).stream().map(ItemDto::toItemDto).collect(Collectors.toList());
    }

    @GetMapping
    private List<ItemDto> getAll(@RequestHeader(value = "X-Sharer-User-Id") int userId) {
        Collection<Item> all = itemService.getAll(userId);

        return all.stream().sorted(Comparator.comparingInt(Item::getId)).map(item -> {

            Set<Booking> bookings = item.getBookings();

            LocalDateTime now = LocalDateTime.now();

            Booking lastBooking = null;
            Booking nextBooking = null;

            if(bookings!= null && !bookings.isEmpty() && item.getOwner().getId().equals(userId)) {
                lastBooking = getLastBooking(bookings, now);
                nextBooking = getNextBooking(bookings, now);
            }

            return new ItemDtoWithNearestBooking(item, lastBooking, nextBooking);
        }).collect(Collectors.toList());
    }

    @PatchMapping("/{id}")
    private ItemDto update(@PathVariable("id") int id,
                           @RequestHeader(value = "X-Sharer-User-Id") Integer userId,
                           @Validated(NullAllowed.class) @RequestBody ItemDto itemDto,
                           BindingResult result) {
        if (result.getErrorCount() != 0) {
            log.error("Validation errors: {}", result.getAllErrors());
            throw new ValidationException();
        }

        return toItemDto(itemService.update(id, itemDto, userId));
    }

    @DeleteMapping("/{id}")
    private void delete(@PathVariable("id") int id) {
        itemService.delete(id);
    }


    @PostMapping("/{id}/comment")
    public CommentDto create(@PathVariable("id") int id,
                             @RequestHeader(value = "X-Sharer-User-Id") int userId,
                             @Valid @RequestBody CommentDto commentDto,
                             BindingResult result) {
        if (result.getErrorCount() != 0) {
            log.error("Validation errors: {}", result.getAllErrors());
            throw new ValidationException();
        }

        return toCommentDto(itemService.create(toComment(commentDto, userId, id)));
    }
}
