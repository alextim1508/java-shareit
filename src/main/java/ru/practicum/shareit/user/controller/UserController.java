package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.validator.NullAllowed;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.controller.UserDto.toUser;
import static ru.practicum.shareit.user.controller.UserDto.toUserDto;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto,
                           BindingResult result) {
        if (result.getErrorCount() != 0) {
            log.error("Validation errors: {}", result.getAllErrors());
            throw new ValidationException();
        }

        return toUserDto(userService.create(toUser(userDto)));
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable("id") int id) {
        return toUserDto(userService.getById(id));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll().stream().map(UserDto::toUserDto).collect(Collectors.toList());
    }

    @PatchMapping("/{id}")
    public UserDto update(@Validated(NullAllowed.class) @RequestBody UserDto userDto,
                           @PathVariable("id") int id,
                           BindingResult result) {
        if (result.getErrorCount() != 0) {
            log.error("Validation errors: {}", result.getAllErrors());
            throw new ValidationException();
        }

        return toUserDto(userService.update(id, userDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        userService.delete(id);
    }
}
