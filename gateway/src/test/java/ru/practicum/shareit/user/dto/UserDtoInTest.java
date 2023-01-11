package ru.practicum.shareit.user.dto;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoInTest {


    @Autowired
    private JacksonTester<UserDtoIn> jacksonTester;

    UserDtoIn userDtoIn;

    @BeforeEach
    protected void setUp() {
        userDtoIn = UserDtoIn.builder()
                .name("name")
                .email("user@mail.com")
                .build();
    }

    @Test
    void toUserDto() throws IOException {
        JsonContent<UserDtoIn> result = jacksonTester.write(userDtoIn);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userDtoIn.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userDtoIn.getEmail());
    }

    @Test
    void equalsAndHashCodeTest() {
        UserDtoIn x = UserDtoIn.builder()
                .name(userDtoIn.getName())
                .email(userDtoIn.getEmail())
                .build();

        UserDtoIn y = UserDtoIn.builder()
                .name(userDtoIn.getName())
                .email(userDtoIn.getEmail())
                .build();

        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void equals_shouldReturnFalseWhenNamesAreNotTheSame() {
        UserDtoIn x = UserDtoIn.builder()
                .name(userDtoIn.getName())
                .email(userDtoIn.getEmail())
                .build();

        UserDtoIn y = UserDtoIn.builder()
                .name("other name")
                .email(userDtoIn.getEmail())
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseWhenEmailsAreNotTheSame() {
        UserDtoIn x = UserDtoIn.builder()
                .name(userDtoIn.getName())
                .email(userDtoIn.getEmail())
                .build();

        UserDtoIn y = UserDtoIn.builder()
                .name(userDtoIn.getName())
                .email("other@mail.com")
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equalsTest() {
        assertThat(userDtoIn.equals(userDtoIn)).isTrue();
        assertThat(userDtoIn.equals(null)).isFalse();
        assertThat(userDtoIn.equals(new Object())).isFalse();
    }

    @Test
    void noArgsConstructorTest() {
        UserDtoIn userDto = new UserDtoIn();
        userDto.setName(userDtoIn.getName());
        userDto.setEmail(userDtoIn.getEmail());

        assertThat(userDto.getName()).isEqualTo(userDtoIn.getName());
        assertThat(userDto.getName()).isEqualTo(userDtoIn.getName());
    }

    @Test
    void allArgsConstructorTest() {
        UserDtoIn userDto = new UserDtoIn(userDtoIn.getName(), userDtoIn.getEmail());

        assertThat(userDto.getName()).isEqualTo(userDtoIn.getName());
        assertThat(userDto.getName()).isEqualTo(userDtoIn.getName());
    }

    @Test
    void toStringTest() {
        assertThat(userDtoIn.toString()).startsWith(userDtoIn.getClass().getSimpleName());
    }
}
