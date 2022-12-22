package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.controller.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    Item create(Item item);

    Collection<Item> getAll(int userId);

    Collection<Item> getAll(String pattern, Boolean isAvailable);

    Item getById(int id);

    Item update(int id, ItemDto itemDto, int userId);

    void delete(int id);
}
