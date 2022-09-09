package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper mapper;

    User user;
    UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .name("name")
                .email("mail@gmail.com")
                .build();

        userDto = UserDto.builder()
                .id(1)
                .name("name")
                .email("mail@gmail.com")
                .build();
    }

    @Test
    void toDto_shouldInvokeServiceAndReturnTheSame() {
        assertThat(mapper.toDto(user)).isEqualTo(userDto);
    }

    @Test
    void toDto_shouldInvokeServiceAndReturnNull() {
        User user = null;
        assertThat(mapper.toDto(user)).isNull();
    }

    @Test
    void toDtoList_shouldInvokeServiceAndReturnTheSame() {
        assertThat(mapper.toDto(List.of(user))).isEqualTo(List.of(userDto));
    }

    @Test
    void toDtoList_shouldInvokeServiceAndReturnNull() {
        List<User> users = null;
        assertThat(mapper.toDto(users)).isNull();
    }

    @Test
    void fromDto_shouldInvokeServiceAndReturnTheSame() {
        assertThat(mapper.fromDto(userDto)).isEqualTo(user);
    }

    @Test
    void fromDto_shouldInvokeServiceAndReturnNull() {
        UserDto userDto = null;
        assertThat(mapper.fromDto(userDto)).isNull();
    }

    @Test
    void updateUserFromDto_shouldInvokeServiceAndReturnUserWithUpdatedId() {
        UserDto userDto = UserDto.builder()
                .id(user.getId() + 1)
                .build();

        User expected = User.builder()
                .id(userDto.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        mapper.updateUserFromDto(userDto, user);

        assertThat(user).isEqualTo(expected);
    }

    @Test
    void updateUserFromDto_shouldInvokeServiceAndReturnUserWithUpdatedName() {
        UserDto userDto = UserDto.builder()
                .name("newName")
                .build();

        User expected = User.builder()
                .id(user.getId())
                .name(userDto.getName())
                .email(user.getEmail())
                .build();

        mapper.updateUserFromDto(userDto, user);

        assertThat(user).isEqualTo(expected);
    }

    @Test
    void updateUserFromDto_shouldInvokeServiceAndReturnUserWithUpdatedEmail() {
        UserDto userDto = UserDto.builder()
                .email("newEmail@gmail.com")
                .build();

        User expected = User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(userDto.getEmail())
                .build();

        mapper.updateUserFromDto(userDto, user);

        assertThat(user).isEqualTo(expected);
    }
}
