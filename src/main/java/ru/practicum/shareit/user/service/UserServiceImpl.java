package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.exception.ConflictException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    private final UserMapper userMapper;

    @Override
    public User create(User user) {
        User savedUser;
        try {
            savedUser = repo.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Email already in use");
        }
        log.info("{} is saved", savedUser);
        return savedUser;

    }

    @Override
    public void existenceCheck(int id) {
        if (!repo.existsById(id)) {
            log.warn("User with ID {} doesn't exist", id);
            throw new NotFoundException("User with ID " + id + " doesn't exist");
        }
    }

    @Override
    public User getById(int id) {
        Optional<User> user = repo.findById(id);
        if (user.isEmpty()) {
            log.warn("User with ID {} is not found", id);
            throw new NotFoundException("User with ID " + id + " is not found");
        }

        log.info("{} is found", user.get());
        return user.get();
    }

    @Override
    public List<User> getAll() {
        List<User> users = repo.findAll();
        log.info("{} users are founded", users.size());
        return users;
    }

    @Override
    public User update(int id, UserDto userDto) {
        User user = getById(id);
        userMapper.updateUserFromDto(userDto, user);

        try {
            repo.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Email already in use");
        }

        log.info("{} is updated", user);
        return user;
    }

    @Override
    public void delete(int id) {
        try {
            repo.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("User with ID " + id + " is not found");
        }
        log.info("User with ID {} is removed", id);
    }
}
