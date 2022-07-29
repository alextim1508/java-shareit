package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Repository
@Slf4j
public class ItemRepositoryInMemory implements ItemRepository {

    private int id;

    private final Map<Integer, Item> itemsById = new HashMap<>();

    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(++id);

            itemsById.put(item.getId(), item);
            log.info("{} is saved", item);
        } else {
            itemsById.put(item.getId(), item);
            log.info("{} is updated", item);
        }

        return item;
    }

    @Override
    public Item findById(int id) {
        if (!itemsById.containsKey(id)) {
            log.error("Item with ID {} does not exist", id);
            throw new RuntimeException("Item does not exist");
        }

        Item itemById = itemsById.get(id);
        log.info("{} is found", itemById);
        return new Item(itemById.getId(), itemById.getName(), itemById.getDescription(), itemById.getAvailable(), itemById.getOwnerId(), itemById.getRequestId());
    }

    @Override
    public Collection<Item> findAll() {
        return itemsById.values();
    }

    @Override
    public void remove(int id) {
        if (!itemsById.containsKey(id)) {
            log.error("Item with ID {} does not exist", id);
            throw new RuntimeException("Item does not exist");
        }

        Item itemById = findById(id);
        itemsById.remove(itemById.getId());
        log.info("{} is deleted", itemById);
    }
}
