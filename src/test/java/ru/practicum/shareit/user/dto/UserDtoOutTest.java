package ru.practicum.shareit.user.dto;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.UserBaseTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoOutTest extends UserBaseTest {

    @Autowired
    private JacksonTester<UserDtoOut> jacksonTester;

    @Test
    void toUserDto() throws IOException {
        JsonContent<UserDtoOut> result = jacksonTester.write(userDtoOut);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(userDtoOut.getId());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userDtoOut.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userDtoOut.getEmail());
    }

    @Test
    void equalsAndHashCodeTest() {
        UserDtoOut x = UserDtoOut.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        UserDtoOut y = UserDtoOut.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void equalsTest() {
        assertThat(userDtoOut.equals(userDtoOut)).isTrue();
        assertThat(userDtoOut.equals(null)).isFalse();
        assertThat(userDtoOut.equals(new Object())).isFalse();
    }

    @Test
    void noArgsConstructorTest() {
        UserDtoOut userDto = new UserDtoOut();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getName()).isEqualTo(user.getName());
        assertThat(userDto.getName()).isEqualTo(user.getName());
    }

    @Test
    void toStringTest() {
        assertThat(userDtoOut.toString()).startsWith(userDtoOut.getClass().getSimpleName());
    }
}