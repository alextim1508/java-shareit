package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutAbs;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.user.controller.UserController.USER_ID_HEADER;
import static ru.practicum.shareit.util.Util.DEFAULT_PAGE_SIZE;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDtoOutAbs create(@RequestHeader(value = USER_ID_HEADER) int userId,
                                       @Valid @RequestBody ItemRequestDtoIn itemRequestDtoIn) {
        return itemRequestService.create(itemRequestDtoIn, userId);
    }

    @GetMapping("/{id}")
    public ItemRequestDtoOutAbs getById(@PathVariable("id") int id,
                                        @RequestHeader(value = USER_ID_HEADER) int userId) {
        return itemRequestService.getById(id, userId);
    }

    @GetMapping
    public List<? extends ItemRequestDtoOutAbs> getAllByRequestor(
            @RequestHeader(value = USER_ID_HEADER) int requestorId) {
        return itemRequestService.getAllByRequestor(requestorId);
    }

    @GetMapping("all")
    public List<? extends ItemRequestDtoOutAbs> getAll(
            @Min(0) @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @Min(1) @RequestParam(value = "size", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
            @RequestHeader(value = USER_ID_HEADER) int requestorId) {
        return itemRequestService.getAll(requestorId, from, size);
    }
}
