package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.dto.UserDtoIn;
import ru.practicum.shareit.user.dto.UserDtoOut;
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

    UserDtoIn userDtoIn1;
    UserDtoIn userDtoIn2;

    @BeforeEach
    void setUp() {
        userDtoIn1 = UserDtoIn.builder()
                .name("user1")
                .email("mail1@gmail.com")
                .build();
        userDtoIn2 = UserDtoIn.builder()
                .name("user2")
                .email("mail2@gmail.com")
                .build();
    }

    @Test
    public void shouldSaveUser() {
        UserDtoOut savedUser = userService.create(userDtoIn1);

        assertThat(userService.getById(savedUser.getId())).isEqualTo(savedUser);
    }

    @Test
    public void shouldThrowExceptionWithDuplicateEmail() {
        userService.create(userDtoIn1);

        userDtoIn2.setEmail(userDtoIn1.getEmail());
        assertThatThrownBy(() -> userService.create(userDtoIn2))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    public void shouldThrowExceptionWhenUserIsMissing() {
        assertThatThrownBy(() -> userService.getById(1))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void shouldGetAllUsers() {
        UserDtoOut savedUser1 = userService.create(userDtoIn1);
        UserDtoOut savedUser2 = userService.create(userDtoIn2);
        assertThat(userService.getAll())
                .hasSize(2)
                .containsAll(Arrays.asList(savedUser1, savedUser2));
    }

    @Test
    public void shouldUpdateUser() {
        UserDtoOut savedUser = userService.create(userDtoIn1);

        userService.update(savedUser.getId(), UserDtoIn.builder().name("newName").build());
        assertThat(userService.getById(savedUser.getId()))
                .extracting(UserDtoOut::getName)
                .isEqualTo("newName");

        userService.update(savedUser.getId(), UserDtoIn.builder().email("newEail@gmail.com").build());
        assertThat(userService.getById(savedUser.getId()))
                .extracting(UserDtoOut::getEmail)
                .isEqualTo("newEail@gmail.com");
    }

    @Test
    public void shouldDeleteUser() {
        UserDtoOut savedUser1 = userService.create(userDtoIn1);
        UserDtoOut savedUser2 = userService.create(userDtoIn2);

        userService.delete(savedUser1.getId());

        assertThat(userService.getAll())
                .hasSize(1)
                .contains(savedUser2);
    }
}
