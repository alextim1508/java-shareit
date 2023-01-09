package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDtoIn;
import ru.practicum.shareit.util.ErrorHandler;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserController controller;

    @MockBean
    private UserClient client;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    UserDtoIn userDtoIn;

    @BeforeEach
    protected void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ErrorHandler.class)
                .build();

        userDtoIn = UserDtoIn.builder()
                .name("name")
                .email("user@mail.com")
                .build();
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerOK() {
        when(client.post(anyString()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenNameIsNull() {
        userDtoIn.setName(null);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenNameIsEmpty() {
        userDtoIn.setName("");

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenNameIsBlank() {
        userDtoIn.setName(" ");

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenEmailIsInvalid() {
        userDtoIn.setEmail("is_not_email");

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getById_shouldAnswerOk() {
        when(client.get(anyString()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAll_shouldAnswerOK() {
        when(client.get())
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void update_shouldAnswerOKWhenNameAndEmailAreNew() {
        when(client.patch(anyString(), anyString()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        userDtoIn.setName("newName");
        userDtoIn.setEmail("newEmail@mail.com");

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void update_shouldAnswerOKWhenNameIsNull() {
        when(client.patch(anyString(), anyString()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        userDtoIn.setName("newName");
        userDtoIn.setEmail(null);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void update_shouldReturnBadRequestWhenNameIsEmpty() {
        userDtoIn.setName("");
        userDtoIn.setEmail(null);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void update_shouldReturnBadRequestWhenNameIsBlank() {
        userDtoIn.setName("  ");
        userDtoIn.setEmail(null);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void update_shouldAnswerOKWhenEmailIsNew() {
        when(client.patch(anyString(), anyString()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        userDtoIn.setName(null);
        userDtoIn.setEmail("newEmail@mail.com");

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void update_shouldReturnBadRequestWhenEmailIsInvalid() {
        userDtoIn.setName(null);
        userDtoIn.setEmail("invalid_email");

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void delete_shouldAnswerOK() {
        when(client.delete(anyString()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}