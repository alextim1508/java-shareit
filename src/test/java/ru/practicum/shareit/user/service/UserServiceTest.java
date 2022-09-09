package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.exception.ConflictException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserMapper userMapper;

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
    void create_shouldInvokeRepositoryAndReturnTheSame() {
        when(userRepository.save(user))
                .thenReturn(user);

        User savedUser = userService.create(user);

        verify(userRepository, times(1)).save(user);

        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    void create_shouldThrowConflictExceptionWithDuplicateEmail() {
        when(userRepository.save(any()))
                .thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> userService.create(user))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Email already in use");

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void existenceCheck_shouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.existsById(anyInt()))
                .thenReturn(false);

        assertThatThrownBy(() -> userService.existenceCheck(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with ID 1 doesn't exist");

        verify(userRepository, times(1)).existsById(1);
    }

    @Test
    void existenceCheck_shouldInvokeRepository() {
        when(userRepository.existsById(anyInt()))
                .thenReturn(true);

        userService.existenceCheck(1);

        verify(userRepository, times(1)).existsById(1);
    }

    @Test
    void getById_shouldThrowNotFoundExceptionWhenRepositoryReturnEmpty() {
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with ID 1 is not found");

        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void getById_shouldInvokeRepositoryAndReturnTheSame() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        User userById = userService.getById(1);

        assertThat(userById).isEqualTo(user);

        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void getAll_shouldInvokeRepositoryAndReturnTheSame() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAll();

        assertThat(users).isNotNull().isNotEmpty().containsAll(List.of(user));

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void update_shouldGetFromRepositoryAndPatchAndSaveAndReturnSaved() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        doNothing().when(userMapper)
                .updateUserFromDto(any(UserDto.class), any(User.class));

        User updatedUser = userService.update(1, userDto);

        assertThat(updatedUser).isEqualTo(user);

        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void update_shouldThrowNotFoundExceptionWhenRepositoryReturnEmpty() {
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(1, userDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with ID 1 is not found");

        verify(userRepository, times(1)).findById(1);

        verify(userRepository, never()).save(user);
    }

    @Test
    void update_shouldThrowConflictExceptionWithDuplicateEmail() {
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
                .thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> userService.update(1, userDto))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Email already in use");

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void delete_shouldInvokeRepositoryDelete() {
        doNothing().when(userRepository)
                .deleteById(anyInt());

        userService.delete(1);

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void delete_shouldThrowNotFoundExceptionWhenInvokeRepositoryWithWrongId() {
        doThrow(EmptyResultDataAccessException.class).when(userRepository)
                .deleteById(anyInt());

        assertThatThrownBy(() -> userService.delete(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with ID 1 is not found");

        verify(userRepository, times(1)).deleteById(1);
    }
}