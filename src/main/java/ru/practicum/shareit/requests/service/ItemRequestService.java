package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest create(ItemRequest itemRequest);

    ItemRequest getById(int id, int userId);

    List<ItemRequest> getAllByRequestor(int requestorId);

    List<ItemRequest>  getAll(int requestorId, int from, int size);
}
