package ru.practicum.shareit.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestMapper;
import ru.practicum.shareit.requests.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.user.controller.UserController.USER_ID_HEADER;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    private final ItemRequestMapper itemRequestMapper;
    private static final String DEFAULT_PAGE_SIZE = "100";

    @PostMapping
    public ItemRequestDto create(@RequestHeader(value = USER_ID_HEADER) int userId,
                                 @Valid @RequestBody ItemRequestDto itemRequestDto,
                                 BindingResult result) {
        if (result.getErrorCount() != 0) {
            log.error("Validation errors: {}", result.getAllErrors());
            throw new ValidationException();
        }
        ItemRequest itemRequest = itemRequestMapper.fromDto(itemRequestDto, userId);
        return itemRequestMapper.toDto(itemRequestService.create(itemRequest));
    }

    @GetMapping("/{id}")
    public ItemRequestDto getById(@PathVariable("id") int id,
                                  @RequestHeader(value = USER_ID_HEADER) int userId) {
        ItemRequest byId = itemRequestService.getById(id, userId);

        return itemRequestMapper.toDto(byId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllByRequestor(@RequestHeader(value = USER_ID_HEADER) int requestorId) {
        List<ItemRequest> itemRequests = itemRequestService.getAllByRequestor(requestorId);
        return itemRequestMapper.toDto(itemRequests);
    }

    @GetMapping("all")
    public List<ItemRequestDto> getAll(@Min(0) @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                       @Min(1) @RequestParam(value = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                       @RequestHeader(value = USER_ID_HEADER) int requestorId) {
        List<ItemRequest> itemRequests = itemRequestService.getAll(requestorId, from, size);
        return itemRequestMapper.toDto(itemRequests);
    }
}
