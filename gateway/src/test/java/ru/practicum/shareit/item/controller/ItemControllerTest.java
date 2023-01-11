package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
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
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.util.ErrorHandler;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.Util.USER_ID_HEADER;

@SpringBootTest
public class ItemControllerTest {

    @Autowired
    private ItemController controller;

    @MockBean
    private ItemClient client;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    ItemDtoIn itemDtoIn;
    CommentDtoIn commentDtoIn;

    @BeforeEach
    protected void setUp() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ErrorHandler.class)
                .build();

        itemDtoIn = ItemDtoIn.builder()
                .name("name")
                .description("description")
                .available(true)
                .requestId(1)
                .build();

        commentDtoIn = new CommentDtoIn("comment");
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerOK() {
        when(client.post(anyString(), anyInt(), anyString()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenNameIsNull() {
        itemDtoIn.setName(null);

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenNameIsEmpty() {
        itemDtoIn.setName("");

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenNameIsBlank() {
        itemDtoIn.setName("  ");

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenDescriptionIsNull() {
        itemDtoIn.setDescription(null);

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenDescriptionIsEmpty() {
        itemDtoIn.setDescription("");

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenDescriptionIsBlank() {
        itemDtoIn.setDescription("  ");

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenAvailableIsNull() {
        itemDtoIn.setAvailable(null);

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenUserIdHeaderIsMissing() {
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoIn))
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

        mvc.perform(get("/items/1")
                        .header(USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getById_shouldAnswerBadRequestWhenUserIdHeaderIsMissing() {
        when(client.get(anyString(), anyInt(), any()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/items/1")
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

        mvc.perform(get("/items")
                        .header(USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAll_shouldAnswerBadRequestWhenUserIdHeaderIsMissing() {
        when(client.get(anyString(), anyInt(), anyMap()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @SneakyThrows
    @Test
    void searchByKeyword_shouldAnswerOK() {
        when(client.get(anyString(), anyInt(), anyMap()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/items/search")
                        .param("text", "text")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void searchByKeyword_shouldAnswerOKWhenTextParamIsMissing() {
        when(client.get(anyString(), anyInt(), anyMap()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void update_shouldAnswerOKWhenNameAndDescriptionAndAvailableAreNew() {
        when(client.patch(anyString(), anyInt(), anyString()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        itemDtoIn.setName("newName");
        itemDtoIn.setDescription("newDescription");
        itemDtoIn.setAvailable(false);

        mvc.perform(patch("/items/1")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void update_shouldAnswerOKWhenNameIsNew() {
        when(client.patch(anyString(), anyInt(), anyString()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        itemDtoIn.setName("newName");
        itemDtoIn.setDescription(null);
        itemDtoIn.setAvailable(null);

        mvc.perform(patch("/items/1")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void update_shouldAnswerBadRequestWhenNameIsEmpty() {
        itemDtoIn.setName("");
        itemDtoIn.setDescription(null);
        itemDtoIn.setAvailable(null);

        mvc.perform(patch("/items/1")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void update_shouldAnswerBadRequestWhenNameIsBlank() {
        itemDtoIn.setName("   ");
        itemDtoIn.setDescription(null);
        itemDtoIn.setAvailable(null);

        mvc.perform(patch("/items/1")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void update_shouldAnswerOKWhenDescriptionIsNew() {
        when(client.patch(anyString(), anyInt(), anyString()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        itemDtoIn.setName(null);
        itemDtoIn.setDescription("newDescription");
        itemDtoIn.setAvailable(null);

        mvc.perform(patch("/items/1")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void update_shouldAnswerBadRequestWhenDescriptionIsEmpty() {
        itemDtoIn.setName(null);
        itemDtoIn.setDescription("");
        itemDtoIn.setAvailable(null);

        mvc.perform(patch("/items/1")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void update_shouldAnswerBadRequestWhenDescriptionIsBlank() {
        itemDtoIn.setName(null);
        itemDtoIn.setDescription("  ");
        itemDtoIn.setAvailable(null);

        mvc.perform(patch("/items/1")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void update_shouldAnswerOKWhenAvailableIsNew() {
        itemDtoIn.setName(null);
        itemDtoIn.setDescription(null);
        itemDtoIn.setAvailable(false);

        mvc.perform(patch("/items/1")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void createComment_shouldAnswerOK() {
        when(client.post(anyString(), anyInt(), anyString()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(post("/items/1/comment")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(commentDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void createComment_shouldAnswerBadRequestWhenNameIsNull() {
        commentDtoIn.setText("");

        mvc.perform(post("/items/1/comment")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(commentDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void createComment_shouldAnswerBadRequestWhenNameIsEmpty() {
        commentDtoIn.setText("");

        mvc.perform(post("/items/1/comment")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(commentDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void createComment_shouldAnswerBadRequestWhenNameIsBlank() {
        commentDtoIn.setText("  ");

        mvc.perform(post("/items/1/comment")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(commentDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}