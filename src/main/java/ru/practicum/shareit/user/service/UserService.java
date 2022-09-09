package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User create(User user);

    User getById(int id);

    List<User> getAll();

    void existenceCheck(int id);

    User update(int id, UserDto user);

    void delete(int id);
}
