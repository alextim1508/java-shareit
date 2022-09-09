package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.util.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class ItemRequestFactory {

    private final ItemRequestRepository itemRequestRepository;

    public ItemRequest getById(int requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
    }
}

