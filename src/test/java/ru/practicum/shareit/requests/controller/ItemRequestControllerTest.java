package ru.practicum.shareit.requests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.ErrorHandler;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.user.controller.UserController.USER_ID_HEADER;

@SpringBootTest
public class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ItemRequestController controller;

    @MockBean
    UserService userService;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;


    User requestor;
    ItemRequest itemRequest;
    ItemRequestDto itemRequestDto;

    LocalDateTime now;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ErrorHandler.class)
                .build();

        now = LocalDateTime.now().withNano(0);

        requestor = User.builder()
                .id(1)
                .name("requestor")
                .email("requestor@gmail.com")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1)
                .description("description")
                .created(now)
                .requestor(requestor)
                .items(Collections.emptyList())
                .build();

        itemRequestDto = new ItemRequestDto("description");
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerOK() {
        when(itemRequestService.create(any(ItemRequest.class)))
                .thenReturn(itemRequest);

        when(userService.getById(anyInt()))
                .thenReturn(requestor);

        mvc.perform(post("/requests")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println("response = " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(itemRequest.getId())))
                .andExpect(jsonPath("$.description", Matchers.is(itemRequest.getDescription())))
                .andExpect(jsonPath("$.created", Matchers.is(itemRequest.getCreated().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.items", Matchers.empty()));
    }

    @SneakyThrows
    @Test
    void create_shouldReturnBadRequestWhenDescriptionIsBlank() {
        itemRequestDto.setDescription(" ");

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldReturnBadRequestWhenDescriptionIsEmpty() {
        itemRequestDto.setDescription("");

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @SneakyThrows
    @Test
    void getById_shouldAnswerOK() {
        when(itemRequestService.getById(anyInt(), anyInt()))
                .thenReturn(itemRequest);

        mvc.perform(get("/requests/1")
                        .header(USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(itemRequest.getId())))
                .andExpect(jsonPath("$.description", Matchers.is(itemRequest.getDescription())))
                .andExpect(jsonPath("$.created", Matchers.is(itemRequest.getCreated().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.items", Matchers.empty()));
    }

    @SneakyThrows
    @Test
    void getAllByRequestor_shouldAnswerOK() {
        when(itemRequestService.getAllByRequestor(anyInt()))
                .thenReturn(Collections.singletonList(itemRequest));


        mvc.perform(get("/requests")
                        .header(USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println("response = " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(itemRequest.getId())))
                .andExpect(jsonPath("$[0].description", Matchers.is(itemRequest.getDescription())))
                .andExpect(jsonPath("$[0].created", Matchers.is(itemRequest.getCreated().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].items", Matchers.empty()));
    }

    @SneakyThrows
    @Test
    void getAll_shouldAnswerOK() {
        when(itemRequestService.getAll(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(itemRequest));


        mvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, "1")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println("response = " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(itemRequest.getId())))
                .andExpect(jsonPath("$[0].description", Matchers.is(itemRequest.getDescription())))
                .andExpect(jsonPath("$[0].created", Matchers.is(itemRequest.getCreated().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].items", Matchers.empty()));
    }
}
