package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.controller.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepo;
    private final UserRepository userRepo;

    @Override
    public Item create(Item item) {
        checkingUserForExistence(item.getOwnerId());
        return itemRepo.save(item);
    }

    private void checkingUserForExistence(int id) {
        userRepo.findById(id);
    }

    @Override
    public Collection<Item> getAll(int userId) {
        return itemRepo.findAll().stream().filter(item -> item.getOwnerId() == userId).collect(Collectors.toList());
    }

    @Override
    public Collection<Item> getAll(String pattern, Boolean isAvailable) {
        if(pattern == null || pattern.isBlank() || pattern.isEmpty())
            return Collections.emptyList();

        String lowerCasePattern = pattern.toLowerCase();
        return itemRepo.findAll().stream().filter(item ->
                item.getAvailable() && (
                        item.getName().toLowerCase().contains(lowerCasePattern) ||
                        item.getDescription().toLowerCase().contains(lowerCasePattern)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Item getById(int id) {
        return itemRepo.findById(id);
    }

    @Override
    public Item update(int id, ItemDto itemDto, int userId) {
        Item item = getById(id);

        checkingUserForOwnership(item.getOwnerId(), userId);

        if(itemDto.getName() != null)
            item.setName(itemDto.getName());
        if(itemDto.getDescription() != null)
            item.setDescription(itemDto.getDescription());
        if(itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());

        return itemRepo.save(item);
    }

    private void checkingUserForOwnership(int ownerId, int userId) {
        if(ownerId != userId)
            throw new IllegalStateException();
    }

    @Override
    public void delete(int id) {
        itemRepo.remove(id);
    }
}
