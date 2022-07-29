package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@Slf4j
public class UserRepositoryInMemory implements UserRepository {

    private int id;

    private final Map<Integer, User> usersById = new HashMap<>();
    private final Set<String> uniqEmails = new HashSet<>();

    @Override
    public User save(User user) {
        if(user.getId() == null) {
            if(uniqEmails.contains(user.getEmail())) {
                log.error("Email of {} already exists", user);
                throw new RuntimeException("Email already exists");
            }
            user.setId(++id);

            uniqEmails.add(user.getEmail());
            usersById.put(user.getId(), user);
            log.info("{} is saved", user);
        } else {
            String oldEmail = findById(user.getId()).getEmail();

            if(uniqEmails.contains(user.getEmail()) && !oldEmail.equals(user.getEmail())) {
                log.error("Email of {} already exists", user);
                throw new RuntimeException("Email already exists");
            }

            uniqEmails.remove(oldEmail);
            uniqEmails.add(user.getEmail());
            usersById.put(user.getId(), user);
            log.info("{} is updated", user);
        }

        return user;
    }

    @Override
    public User findById(int id) {
        if(!usersById.containsKey(id)) {
            log.error("User with ID {} does not exist", id);
            throw new IllegalStateException("User does not exist");
        }

        User userById = usersById.get(id);
        log.info("{} is found", userById);
        return new User(userById.getId(), userById.getName(),userById.getEmail());
    }

    @Override
    public Collection<User> findAll() {
        return usersById.values();
    }

    @Override
    public void remove(int id) {
        if(!usersById.containsKey(id)) {
            log.error("User with ID {} does not exist", id);
            throw new IllegalStateException("User does not exist");
        }

        User userById = findById(id);
        usersById.remove(userById.getId());
        uniqEmails.remove(userById.getEmail());
        log.info("{} is deleted", userById);
    }
}
