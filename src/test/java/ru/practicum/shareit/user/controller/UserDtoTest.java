package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    static UserDto userDto = UserDto.builder()
            .id(1)
            .name("user")
            .email("user@mail.ru")
            .build();

    @Test
    void toUserDto() throws IOException {
        JsonContent<UserDto> result = jacksonTester.write(userDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(userDto.getId());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
    }
}