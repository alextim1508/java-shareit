package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.UserBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest extends UserBaseTest {


    @Test
    void testEquals() {
        User x = User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        User y = User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void equalsTest() {
        assertThat(user.equals(user)).isTrue();
        assertThat(user.equals(null)).isFalse();
        assertThat(user.equals(new Object())).isFalse();
    }


    @Test
    void testNoArgsConstructorAndGettersSetters() {
        User user = new User();
        user.setId(this.user.getId());
        user.setName(this.user.getName());
        user.setEmail(this.user.getEmail());

        assertThat(user.getId()).isEqualTo(this.user.getId());
        assertThat(user.getName()).isEqualTo(this.user.getName());
        assertThat(user.getEmail()).isEqualTo(this.user.getEmail());
    }

    @Test
    void testBuilderAndToString() {
        User user = User.builder()
                .id(this.user.getId())
                .name(this.user.getName())
                .email(this.user.getEmail())
                .build();

        assertThat(user.toString()).startsWith(User.class.getSimpleName());
    }
}
