package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {

    User create(User user);

    Collection<User> getAll();

    User getById(int id);

    User update(int id, UserDto user);

    void delete(int id);
}
