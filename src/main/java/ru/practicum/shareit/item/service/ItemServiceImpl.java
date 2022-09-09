package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.controller.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.exception.ActionIsNotAvailableException;
import ru.practicum.shareit.util.exception.ForbiddenException;
import ru.practicum.shareit.util.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.util.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepo;
    private final UserRepository userRepo;
    private final CommentRepository commentRepo;

    @Override
    public Item create(Item item) {
        checkingUserForExistence(item.getOwner().getId());

        return itemRepo.save(item);
    }

    private void checkingUserForExistence(int id) {
        if(!userRepo.existsById(id))
            throw new ResourceNotFoundException();
    }

    @Override
    public Collection<Item> getAll(int userId) {
        return itemRepo.getItemByOwner(userId);
    }

    @Override
    public Collection<Item> getAll(String pattern, Boolean isAvailable) {
        if (pattern == null || pattern.isBlank() || pattern.isEmpty())
            return Collections.emptyList();

        String lowerCasePattern = pattern.toLowerCase();
        return itemRepo.findByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(lowerCasePattern, lowerCasePattern);
    }

    @Transactional
    @Override
    public Item getById(int id) {
        Item item = itemRepo.findById(id).orElseThrow(ResourceNotFoundException::new);
        int forLoading = item.getBookings().size();
        forLoading = item.getComments().size();
        return item;
    }

    @Override
    @Transactional
    public Item update(int id, ItemDto itemDto, int userId) {
        Item item = getById(id);

        checkingUserForOwnership(item.getOwner().getId(), userId);

        if (itemDto.getName() != null)
            item.setName(itemDto.getName());
        if (itemDto.getDescription() != null)
            item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());

        return item;
    }

    private void checkingUserForOwnership(int ownerId, int userId) {
        if (ownerId != userId)
            throw new ForbiddenException();
    }

    @Override
    public void delete(int id) {
        itemRepo.deleteById(id);
    }

    @Override
    public Comment create(Comment comment) {
        Item itemById = getById(comment.getItem().getId());

        User userById = userRepo.findById(comment.getAuthor().getId()).orElseThrow(ResourceNotFoundException::new);

        check(itemById.getBookings(), comment.getAuthor().getId());

        Comment save = commentRepo.save(comment);
        save.setAuthor(userById);
        save.setItem(itemById);

        return save;
    }

    private void check(Set<Booking> bookings, int authorId) {
        if(bookings == null || bookings.isEmpty())
            throw new ActionIsNotAvailableException();
        else if(bookings.stream().noneMatch(booking ->
                booking.getBooker().getId().equals(authorId) &&
                        booking.getStatus().equals(BookingStatus.APPROVED) &&
                        booking.getEndDate().isBefore(LocalDateTime.now())))
            throw new ActionIsNotAvailableException();
    }
}
