package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOutAbs;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOutAbs;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.util.Util.USER_ID_HEADER;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDtoOutAbs create(
            @RequestHeader(USER_ID_HEADER) Integer ownerId,
            @RequestBody ItemDtoIn itemDto) {

        return itemService.create(itemDto, ownerId);
    }

    @GetMapping("/{id}")
    public ItemDtoOutAbs getById(
            @RequestHeader(USER_ID_HEADER) Integer ownerId,
            @PathVariable Integer id) {

        return itemService.getById(id, ownerId);
    }

    @GetMapping
    public List<? extends ItemDtoOutAbs> getAll(
            @RequestHeader(USER_ID_HEADER) Integer userId) {

        return itemService.getAvailableItemByOwner(userId);
    }

    @GetMapping("/search")
    public List<? extends ItemDtoOutAbs> searchByKeyword(
            @RequestParam String text) {

        return itemService.getAvailableItemByPattern(text);
    }

    @PatchMapping("/{id}")
    public ItemDtoOutAbs update(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @RequestBody ItemDtoIn itemDto,
            @PathVariable Integer id) {

        return itemService.update(id, itemDto, userId);
    }

    @PostMapping("/{id}/comment")
    public CommentDtoOutAbs create(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @RequestBody CommentDtoIn commentDtoIn,
            @PathVariable Integer id) {

        return itemService.create(commentDtoIn, id, userId);
    }
}
