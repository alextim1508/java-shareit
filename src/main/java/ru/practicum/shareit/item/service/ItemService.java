package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.controller.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item create(Item item);

    Comment create(Comment comment);

    Item getById(int id, int ownerId);

    List<Item> getAvailableItemByOwner(int userId);

    List<Item> getAvailableItemByPattern(String pattern);

    Item update(int id, ItemDto itemDto, int userId);

    void delete(int id);
}
