package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.controller.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.NullAllowed;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.controller.ItemDto.toItem;
import static ru.practicum.shareit.item.controller.ItemDto.toItemDto;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    private ItemDto create(@RequestHeader(value="X-Sharer-User-Id") int userId,
                           @Valid @RequestBody ItemDto itemDto,
                           BindingResult result) {
        if(result.getErrorCount() != 0) {
            log.error("Validation errors: {}", result.getAllErrors());
            throw new ValidationException();
        }

        return toItemDto(itemService.create(toItem(itemDto, userId)));
    }

    @PatchMapping("/{id}")
    private ItemDto update(@PathVariable("id") int id,
                           @RequestHeader(value="X-Sharer-User-Id") int userId,
                           @Validated(NullAllowed.class) @RequestBody ItemDto itemDto,
                           BindingResult result) {
        if(result.getErrorCount() != 0) {
            log.error("Validation errors: {}", result.getAllErrors());
            throw new ValidationException();
        }

        return toItemDto(itemService.update(id, itemDto, userId));
    }

    @GetMapping("/{id}")
    private ItemDto getById(@PathVariable("id") int id) {
        return toItemDto(itemService.getById(id));
    }

    @GetMapping("/search")
    private List<ItemDto> search(@RequestParam("text") String pattern) {
        return itemService.getAll(pattern, true).stream().map(ItemDto::toItemDto).collect(Collectors.toList());
    }

    @GetMapping
    private List<ItemDto> getAll(@RequestHeader(value="X-Sharer-User-Id") int userId) {
        return itemService.getAll(userId).stream().map(ItemDto::toItemDto).collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    private void delete(@PathVariable("id") int id) {
        itemService.delete(id);
    }
}
