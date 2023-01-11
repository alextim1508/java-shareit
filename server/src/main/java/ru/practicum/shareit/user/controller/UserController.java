package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDtoIn;
import ru.practicum.shareit.user.dto.UserDtoOutAbs;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDtoOutAbs create(
            @RequestBody UserDtoIn userDto) {

        return userService.create(userDto);
    }

    @GetMapping("/{id}")
    public UserDtoOutAbs getById(
            @PathVariable Integer id) {

        return userService.getById(id);
    }

    @GetMapping
    public List<? extends UserDtoOutAbs> getAll() {

        return userService.getAll();
    }

    @PatchMapping("/{id}")
    public UserDtoOutAbs update(
            @RequestBody UserDtoIn userDtoIn,
            @PathVariable Integer id) {

        return userService.update(id, userDtoIn);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Integer id) {

        userService.delete(id);
    }
}
