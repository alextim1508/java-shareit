package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOutAbs;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareit.util.Util.USER_ID_HEADER;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDtoOutAbs create(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @RequestBody ItemRequestDtoIn itemRequestDtoIn) {

        return itemRequestService.create(itemRequestDtoIn, userId);
    }

    @GetMapping("/{id}")
    public ItemRequestDtoOutAbs getById(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @PathVariable Integer id) {

        return itemRequestService.getById(id, userId);
    }

    @GetMapping
    public List<? extends ItemRequestDtoOutAbs> getAllByRequestor(
            @RequestHeader(USER_ID_HEADER) Integer userId) {

        return itemRequestService.getAllByRequestor(userId);
    }

    @GetMapping("all")
    public List<? extends ItemRequestDtoOutAbs> getAll(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @RequestParam Integer from,
            @RequestParam Integer size) {

        return itemRequestService.getAll(userId, from, size);
    }
}
