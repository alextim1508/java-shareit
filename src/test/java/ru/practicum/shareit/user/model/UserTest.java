package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.UserBaseTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void testAllArgsConstructorAndGettersSetters() {
        User user = new User(this.user.getId(), this.user.getName(), this.user.getEmail());

        assertThat(user.getId()).isEqualTo(this.user.getId());
        assertThat(user.getName()).isEqualTo(this.user.getName());
        assertThat(user.getEmail()).isEqualTo(this.user.getEmail());
    }

    @Test
    void allArgsConstructor_shouldThrowNotFoundExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> {
            new User(this.user.getId(), null, this.user.getEmail());
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("name is marked non-null but is null");
    }

    @Test
    void allArgsConstructor_shouldThrowNotFoundExceptionWhenEmailIsNull() {
        assertThatThrownBy(() -> {
            new User(this.user.getId(), this.user.getName(), null);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("email is marked non-null but is null");
    }

    @Test
    void builder_shouldThrowNotFoundExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> {
            User.builder()
                    .id(this.user.getId())
                    .name(null)
                    .email(this.user.getEmail())
                    .build();
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("name is marked non-null but is null");
    }

    @Test
    void builder_shouldThrowNotFoundExceptionWhenEmailIsNull() {
        assertThatThrownBy(() -> {
            User.builder()
                    .id(this.user.getId())
                    .name(this.user.getName())
                    .email(null)
                    .build();
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("email is marked non-null but is null");
    }

    @Test
    void setName_shouldThrowNotFoundExceptionWhenNameIsNull() {
        User user = User.builder()
                .id(this.user.getId())
                .name(this.user.getName())
                .email(this.user.getEmail())
                .build();

        assertThatThrownBy(() -> {
            user.setName(null);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("name is marked non-null but is null");
    }

    @Test
    void setDescription_shouldThrowNotFoundExceptionWhenDescriptionIsNull() {
        User user = User.builder()
                .id(this.user.getId())
                .name(this.user.getName())
                .email(this.user.getEmail())
                .build();

        assertThatThrownBy(() -> {
            user.setEmail(null);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("email is marked non-null but is null");
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
