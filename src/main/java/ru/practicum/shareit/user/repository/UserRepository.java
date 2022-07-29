package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {

    User save(User user);

    User findById(int id);

    Collection<User> findAll();

    void remove(int id);
}
