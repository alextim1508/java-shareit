package ru.practicum.shareit.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exception.ConflictException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
public class UserServiceIT {

    @Autowired
    UserService userService;

    @Test
    public void shouldSaveUser() {
        User savedUser = userService.create(new User("user", "mail@gmail.com"));
        assertThat(userService.getById(savedUser.getId())).isEqualTo(savedUser);
    }

    @Test
    public void shouldThrowExceptionWithDuplicateEmail() {
        userService.create(new User("user1", "mail@gmail.com"));
        assertThatThrownBy(() -> {
            userService.create(new User("user2", "mail@gmail.com"));
        }).isInstanceOf(ConflictException.class);
    }

    @Test
    public void shouldThrowExceptionWhenUserIsMissing() {
        assertThatThrownBy(() -> {
            userService.getById(1);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void shouldGetAllUsers() {
        User savedUser1 = userService.create(new User("user", "mail1@gmail.com"));
        User savedUser2 = userService.create(new User("user", "mail2@gmail.com"));
        assertThat(userService.getAll())
                .hasSize(2)
                .containsAll(Arrays.asList(savedUser1, savedUser2));
    }

    @Test
    public void shouldUpdateUser() {
        User savedUser = userService.create(new User("user", "mail@gmail.com"));

        userService.update(savedUser.getId(), new UserDto(null, "newName", null));
        assertThat(userService.getById(savedUser.getId()))
                .extracting(User::getName)
                .isEqualTo("newName");

        userService.update(savedUser.getId(), new UserDto(null, null, "newEail@gmail.com"));
        assertThat(userService.getById(savedUser.getId()))
                .extracting(User::getEmail)
                .isEqualTo("newEail@gmail.com");
    }

    @Test
    public void shouldDeleteUser() {
        User savedUser1 = userService.create(new User("user", "mail1@gmail.com"));
        User savedUser2 = userService.create(new User("user", "mail2@gmail.com"));

        userService.delete(savedUser1.getId());

        assertThat(userService.getAll())
                .hasSize(1)
                .contains(savedUser2);
    }
}
