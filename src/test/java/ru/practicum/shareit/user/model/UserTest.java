package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    void equalsSymmetricTest() {
        User x = new User(1, "user", "user@gmail.com");
        User y = new User(2, "user", "user@gmail.com");

        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void requiredArgsConstructorTest() {
        String name = "user";
        String email = "user@gmail.com";
        User user = new User(name, email);

        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    void noArgsConstructorTest() {
        String name = "user";
        String email = "user@gmail.com";
        User user = new User();

        user.setName(name);
        user.setEmail(email);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
    }
}
