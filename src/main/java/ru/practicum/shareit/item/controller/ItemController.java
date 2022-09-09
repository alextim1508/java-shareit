package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.CommentMapper;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.validator.NullAllowed;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

import static ru.practicum.shareit.user.controller.UserController.USER_ID_HEADER;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    private final ItemMapper itemMapper;

    private final CommentMapper commentMapper;

    @PostMapping
    public ItemDto create(@RequestHeader(value = USER_ID_HEADER) int ownerId,
                          @Valid @RequestBody ItemDto itemDto,
                          BindingResult result) {
        if (result.getErrorCount() != 0) {
            log.error("Validation errors: {}", result.getAllErrors());
            throw new ValidationException();
        }
        Item item = itemMapper.fromDto(itemDto, ownerId);
        return itemMapper.toDto(itemService.create(item));
    }

    @GetMapping("/{id}")
    public ItemDto getById(@RequestHeader(value = USER_ID_HEADER) int ownerId,
                           @PathVariable("id") int id) {
        Item item = itemService.getById(id, ownerId);
        return itemMapper.toDto(item);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(value = USER_ID_HEADER) int userId) {
        List<Item> items = itemService.getAvailableItemByOwner(userId);
        return itemMapper.toDto(items);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String pattern) {
        List<Item> items = itemService.getAvailableItemByPattern(pattern);
        return itemMapper.toDto(items);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable("id") int id,
                          @RequestHeader(value = USER_ID_HEADER) Integer userId,
                          @Validated(NullAllowed.class) @RequestBody ItemDto itemDto) {
        return itemMapper.toDto(itemService.update(id, itemDto, userId));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        itemService.delete(id);
    }


    @PostMapping("/{id}/comment")
    public CommentDto create(@PathVariable("id") int id,
                             @RequestHeader(value = USER_ID_HEADER) int userId,
                             @Valid @RequestBody CommentDto commentDto,
                             BindingResult result) {
        if (result.getErrorCount() != 0) {
            log.error("Validation errors: {}", result.getAllErrors());
            throw new ValidationException();
        }

        Comment comment = commentMapper.fromDto(commentDto, id, userId);
        return commentMapper.toDto(itemService.create(comment));
    }
}
