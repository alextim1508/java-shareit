package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.OffsetLimitPageable;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;
    private final UserService userService;

    @Override
    public ItemRequest create(ItemRequest itemRequest) {
        ItemRequest savedItemRequest = repository.save(itemRequest);
        log.info("{} is saved", savedItemRequest);
        return savedItemRequest;
    }

    @Transactional
    @Override
    public ItemRequest getById(int id, int userId) {
        userService.existenceCheck(userId);

        Optional<ItemRequest> itemRequest = repository.findById(id);
        if (itemRequest.isEmpty()) {
            log.warn("The item request with ID {} is not found", id);
            throw new NotFoundException(ItemRequest.class);
        }

        int forLoading = itemRequest.get().getItems().size();
        return itemRequest.get();
    }

    @Transactional
    @Override
    public List<ItemRequest> getAllByRequestor(int requestorId) {
        userService.existenceCheck(requestorId);

        List<ItemRequest> itemRequests = repository.getItemRequestByRequestor(requestorId);
        int forLoading;
        for (ItemRequest itemRequest : itemRequests)
            forLoading = itemRequest.getItems().size();

        log.info("Found {} item requests", itemRequests.size());

        return itemRequests;
    }

    @Transactional
    @Override
    public List<ItemRequest> getAll(int userId, int from, int size) {
        List<ItemRequest> itemRequests = repository.findAllByRequestorIdNot(
                userId,
                new OffsetLimitPageable(from, size, Sort.by(Sort.Direction.ASC, "created"))
        ).getContent();

        int forLoading;
        for (ItemRequest itemRequest : itemRequests)
            forLoading = itemRequest.getItems().size();

        log.info("Found {} item requests by the requestor with ID {}. From {}, size {}", itemRequests.size(), userId, from, size);

        return itemRequests;
    }
}
