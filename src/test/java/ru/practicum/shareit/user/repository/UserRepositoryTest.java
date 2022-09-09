package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    static User user = User.builder()
            .name("user")
            .email("user@email.com")
            .build();

    @Test
    void existsById_shouldInvokeRepositoryAndReturnTheSame() {
        User savedUser = userRepository.save(user);
        assertThat(userRepository.existsById(savedUser.getId())).isTrue();
    }

    @Test
    void load() {
        Assertions.assertNotNull(entityManager);
    }
}
