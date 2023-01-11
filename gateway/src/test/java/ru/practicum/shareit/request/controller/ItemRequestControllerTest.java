package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.util.ErrorHandler;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.Util.USER_ID_HEADER;

@SpringBootTest
public class ItemRequestControllerTest {

    @Autowired
    private ItemRequestController controller;

    @MockBean
    private ItemClient client;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    ItemRequestDtoIn itemRequestDtoIn;

    @BeforeEach
    protected void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ErrorHandler.class)
                .build();

        itemRequestDtoIn = new ItemRequestDtoIn("description");
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerOK() {
        when(client.post(anyString(), anyInt(), anyString()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(post("/requests")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemRequestDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void create_shouldReturnBadRequestWhenDescriptionIsNull() {
        itemRequestDtoIn.setDescription(null);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldReturnBadRequestWhenDescriptionIsEmpty() {
        itemRequestDtoIn.setDescription("");

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldReturnBadRequestWhenDescriptionIsBlank() {
        itemRequestDtoIn.setDescription(" ");

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getById_shouldAnswerOK() {
        when(client.get(anyString(), anyInt(), any()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/requests/1")
                        .header(USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getById_shouldAnswerBadRequestWhenIdHeaderIsMissing() {
        mvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @SneakyThrows
    @Test
    void getAllByRequestor_shouldAnswerOK() {
        when(client.get(anyString(), anyInt(), any()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/requests")
                        .header(USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAllByRequestor_shouldAnswerBadRequestWhenIdHeaderIsMissing() {
        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAll_shouldAnswerOK() {
        when(client.get(anyString(), anyInt(), anyMap()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, "1")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAll_shouldAnswerBadRequestWhenIdHeaderIsMissing() {
        mvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
