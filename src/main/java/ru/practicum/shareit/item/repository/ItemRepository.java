package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {

    Item save(Item item);

    Item findById(int id);

    Collection<Item> findAll();

    void remove(int id);
}
