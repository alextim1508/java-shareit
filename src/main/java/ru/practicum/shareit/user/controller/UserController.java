package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.validator.NullAllowed;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto,
                          BindingResult result) {
        if (result.getErrorCount() != 0) {
            log.error("Validation errors: {}", result.getAllErrors());
            throw new ValidationException();
        }

        return userMapper.toDto(userService.create(userMapper.fromDto(userDto)));
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable("id") int id) {
        return userMapper.toDto(userService.getById(id));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userMapper.toDto(userService.getAll());
    }

    @PatchMapping("/{id}")
    public UserDto update(@Validated(NullAllowed.class) @RequestBody UserDto userDto,
                          @PathVariable("id") int id) {
        return userMapper.toDto(userService.update(id, userDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        userService.delete(id);
    }
}
