package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.controller.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exception.ActionIsNotAvailableException;
import ru.practicum.shareit.util.exception.ForbiddenException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.booking.service.BookingServiceImpl.getLastBooking;
import static ru.practicum.shareit.booking.service.BookingServiceImpl.getNextBooking;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepo;

    private final CommentRepository commentRepo;

    private final UserService userService;

    private final ItemMapper itemMapper;

    @Override
    public Item create(Item item) {
        userService.existenceCheck(item.getOwner().getId());

        Item savedItem = itemRepo.save(item);
        log.info("{} is saved", savedItem);
        return savedItem;
    }

    @Transactional
    @Override
    public Item getById(int id, int userId) {
        Optional<Item> item = itemRepo.findById(id);
        if (item.isEmpty()) {
            log.warn("The item with ID {} is not found", id);
            throw new NotFoundException("Item with ID " + id + " is not found");
        }

        int forLoading = item.get().getBookings().size();

        if (item.get().getOwner().getId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();
            item.get().setLastBooking(getLastBooking(item.get(), now));
            item.get().setNextBooking(getNextBooking(item.get(), now));
        }

        forLoading = item.get().getComments().size();

        log.info("{} is found", item.get());
        return item.get();
    }

    @Override
    public List<Item> getAvailableItemByOwner(int ownerId) {
        List<Item> items = itemRepo.findByOwner(ownerId);
        LocalDateTime now = LocalDateTime.now();
        items.forEach(item -> {
            if (item.getOwner().getId().equals(ownerId)) {
                item.setLastBooking(getLastBooking(item, now));
                item.setNextBooking(getNextBooking(item, now));
            }
        });

        log.info("Founded {} items by owner with ID {}", items.size(), ownerId);
        return items;
    }

    @Override
    public List<Item> getAvailableItemByPattern(String pattern) {
        if (pattern == null || pattern.isBlank() || pattern.isEmpty())
            return Collections.emptyList();

        List<Item> items = itemRepo.findAvailableItemsByNameOrDescription(pattern);
        log.info("Founded {} items by pattern {}", items.size(), pattern);
        return items;
    }

    @Override
    @Transactional
    public Item update(int id, ItemDto itemDto, int userId) {
        Item item = getById(id, userId);

        if (item.getOwner().getId() != userId) {
            log.info("User with ID {} cannot change {}. Only owner can do it", userId, itemDto);
            throw new ForbiddenException();
        }

        itemMapper.updateItemFromDto(itemDto, item);

        log.info("{} is updated", item);

        return item;
    }

    @Override
    public void delete(int id) {
        try {
            itemRepo.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Item with ID " + id + " is not found");
        }
        log.info("Item with ID {} is removed", id);
    }

    @Override
    public Comment create(Comment comment) {
        bookingCheck(comment.getItem(), comment.getAuthor().getId());

        Comment savedComment = commentRepo.save(comment);

        log.info("{} is saved", savedComment);

        return savedComment;
    }

    private void bookingCheck(Item item, int authorId) {
        if (item.getBookings() == null || item.getBookings().stream().noneMatch(booking ->
                booking.getBooker().getId().equals(authorId) &&
                        booking.getStatus().equals(BookingStatus.APPROVED) &&
                        booking.getEndDate().isBefore(LocalDateTime.now()))) {
            log.warn("User with ID {} did not book {}", authorId, item);
            throw new ActionIsNotAvailableException();
        }
    }
}
