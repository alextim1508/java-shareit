package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    @Override
    public User create(User user) {
        return repo.save(user);
    }

    @Override
    public Collection<User> getAll() {
        return repo.findAll();
    }

    @Override
    public User getById(int id) {
        return repo.findById(id);
    }

    @Override
    public User update(int id, UserDto userDto) {
        User user = getById(id);

        if (userDto.getName() != null)
            user.setName(userDto.getName());
        if (userDto.getEmail() != null)
            user.setEmail(userDto.getEmail());

        return repo.save(user);
    }

    @Override
    public void delete(int id) {
        repo.remove(id);
    }
}
