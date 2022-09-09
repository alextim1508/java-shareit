package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.ErrorHandler;
import ru.practicum.shareit.util.exception.ConflictException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.user.controller.UserController.USER_ID_HEADER;

@SpringBootTest
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private UserController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    User user;
    UserDto userDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ErrorHandler.class)
                .build();

        user = User.builder()
                .id(1)
                .name("user")
                .email("user@gmail.com")
                .build();

        userDto = UserDto.builder()
                .id(1)
                .name("user")
                .email("user@gmail.com")
                .build();
    }

    @Test
    void create_shouldAnswerOK() throws Exception {
        when(userService.create(any()))
                .thenReturn(user);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenNameIsNull() {
        userDto.setName(null);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Validation error")));
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenNameIsBlank() {
        userDto.setName(" ");

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Validation error")));
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenNameIsEmpty() {
        userDto.setName("");

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Validation error")));
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenEmailIsInvalid() {
        userDto.setEmail("is_not_email");

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Validation error")));
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerConflictWhenAddingDuplicatedEmail() {
        when(userService.create(any()))
                .thenThrow(new ConflictException(""));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$", is("Conflict")));
    }

    @SneakyThrows
    @Test
    void getById_shouldAnswerOK() {
        when(userService.getById(1))
                .thenReturn(user);

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @SneakyThrows
    @Test
    void getById_shouldAnswerNotFoundWhenUserIsNotFounded() {
        when(userService.getById(anyInt()))
                .thenThrow(new NotFoundException("User with ID 1 is not found"));

        mvc.perform(get("/users/1")
                        .header(USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Not found")));
    }

    @SneakyThrows
    @Test
    void getAll_shouldAnswerOK() {
        when(userService.getAll())
                .thenReturn(Collections.singletonList(user));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(user.getId())))
                .andExpect(jsonPath("$[0].name", is(user.getName())))
                .andExpect(jsonPath("$[0].email", is(user.getEmail())));
    }

    @SneakyThrows
    @Test
    void update_shouldAnswerOK() {
        when(userService.update(anyInt(), any()))
                .thenReturn(user);

        mvc.perform(patch("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @SneakyThrows
    @Test
    void update_shouldReturnBadRequestWhenEmailIsInvalid() {
        userDto.setId(null);
        userDto.setName(null);
        userDto.setEmail("is_not_email");

        mvc.perform(patch("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Validation error")));
    }

    @Test
    void update_shouldAnswerConflictWhenUpdatingDuplicatedEmail() throws Exception {
        when(userService.update(anyInt(), any()))
                .thenThrow(new ConflictException(""));

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$", is("Conflict")));
    }

    @SneakyThrows
    @Test
    void delete_shouldAnswerOK() {
        doNothing().when(userService)
                .delete(anyInt());

        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void delete_shouldBadRequestWhenUserIsNotFound() {
        doThrow(new NotFoundException(""))
                .when(userService).delete(anyInt());

        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Not found")));
    }
}