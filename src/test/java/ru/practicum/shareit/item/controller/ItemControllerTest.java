package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemFactory;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.ErrorHandler;
import ru.practicum.shareit.util.exception.ForbiddenException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.user.controller.UserController.USER_ID_HEADER;

@SpringBootTest
public class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemFactory itemFactory;

    @Autowired
    private ItemController controller;

    private ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    User owner;
    Item item;
    ItemDto itemDto;

    @BeforeEach
    void setUp() {
        owner = new User("owner", "mail@gmail.com");

        item = Item.builder()
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        itemDto = ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ErrorHandler.class)
                .build();
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerOK() {
        when(itemService.create(any(Item.class)))
                .thenReturn(item);

        when(userService.getById(anyInt()))
                .thenReturn(owner);

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId())))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())));
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenNameIsNull() {
        itemDto.setName(null);

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenNameIsEmpty() {
        itemDto.setName("");

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenNameIsBlank() {
        itemDto.setName("  ");

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenDescriptionIsNull() {
        itemDto.setDescription(null);

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenDescriptionIsEmpty() {
        itemDto.setDescription("");

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenDescriptionIsBlank() {
        itemDto.setDescription("  ");

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenUserIdHeaderIsMissing() {
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getById_shouldAnswerOK() {
        User user = new User("name", "user@gmail.com");

        LocalDateTime now = LocalDateTime.now();
        Booking lastBooking = Booking.builder()
                .startDate(now.minusDays(2))
                .endDate(now.minusDays(1))
                .item(item)
                .booker(user)
                .build();

        Booking nextBooking = Booking.builder()
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .item(item)
                .booker(user)
                .build();

        item.setLastBooking(lastBooking);
        item.setNextBooking(nextBooking);

        when(itemService.getById(anyInt(), anyInt()))
                .thenReturn(item);

        mvc.perform(get("/items/1")
                        .header(USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId())))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())))
                .andExpect(jsonPath("$.lastBooking.id", is(lastBooking.getId())))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(lastBooking.getBooker().getId())))
                .andExpect(jsonPath("$.lastBooking.start", is(lastBooking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.lastBooking.end", is(lastBooking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.nextBooking.id", is(nextBooking.getId())))
                .andExpect(jsonPath("$.nextBooking.bookerId", is(nextBooking.getBooker().getId())))
                .andExpect(jsonPath("$.nextBooking.start", is(nextBooking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.nextBooking.end", is(nextBooking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @SneakyThrows
    @Test
    void getById_shouldAnswerNotFoundWhenItemIsNotFounded() {
        when(itemService.getById(anyInt(), anyInt()))
                .thenThrow(new NotFoundException("Item with ID 1 is not found"));

        mvc.perform(get("/items/1")
                        .header(USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Not found")));
    }

    @SneakyThrows
    @Test
    void getAll_shouldAnswerOKAndReturnFoundedItems() {
        when(itemService.getAvailableItemByPattern(anyString()))
                .thenReturn(Arrays.asList(item));

        mvc.perform(get("/items/search")
                        .param("text", "na")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(item.getId())))
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].available", is(item.getAvailable())));
    }


    @SneakyThrows
    @Test
    void getAll_shouldAnswerOK() {
        User user = new User("name", "user@gmail.com");

        LocalDateTime now = LocalDateTime.now();
        Booking lastBooking = Booking.builder()
                .startDate(now.minusDays(2))
                .endDate(now.minusDays(1))
                .item(item)
                .booker(user)
                .build();

        Booking nextBooking = Booking.builder()
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .item(item)
                .booker(user)
                .build();

        item.setLastBooking(lastBooking);
        item.setNextBooking(nextBooking);

        when(itemService.getAvailableItemByOwner(anyInt()))
                .thenReturn(Collections.singletonList(item));

        mvc.perform(get("/items")
                        .header(USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(item.getId())))
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].available", is(item.getAvailable())))
                .andExpect(jsonPath("$[0].lastBooking.id", is(lastBooking.getId())))
                .andExpect(jsonPath("$[0].lastBooking.bookerId", is(lastBooking.getBooker().getId())))
                .andExpect(jsonPath("$[0].nextBooking.id", is(nextBooking.getId())))
                .andExpect(jsonPath("$[0].nextBooking.bookerId", is(nextBooking.getBooker().getId())));
    }

    @SneakyThrows
    @Test
    void update_shouldAnswerOK() {
        when(itemService.update(anyInt(), any(ItemDto.class), anyInt()))
                .thenReturn(item);

        mvc.perform(patch("/items/1")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId())))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())));
    }

    @SneakyThrows
    @Test
    void update_shouldAnswerNotFoundWhenActionIsForbidden() {
        when(itemService.update(anyInt(), any(ItemDto.class), anyInt()))
                .thenThrow(new ForbiddenException());

        mvc.perform(patch("/items/1")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("ForbiddenException")));
    }

    @SneakyThrows
    @Test
    void delete_shouldAnswerOK() {
        mvc.perform(delete("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void delete_shouldBadRequestWhenUserIsNotFound() {
        doThrow(new NotFoundException("")).when(itemService).delete(anyInt());

        mvc.perform(delete("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Not found")));
    }

    @SneakyThrows
    @Test
    void createComment_shouldAnswerOK() {
        LocalDateTime now = LocalDateTime.now();

        User author = new User(1, "author", "mail@gmail.com");

        Comment comment = new Comment(1, "cool", item, author, now);

        when(itemService.create(any(Comment.class)))
                .thenReturn(comment);

        when(userService.getById(1)).thenReturn(author);
        when(userService.getById(2)).thenReturn(author);
        when(itemFactory.get(anyInt())).thenReturn(item);

        CommentDto commentDto = new CommentDto("cool");

        mvc.perform(post("/items/1/comment")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId())))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.authorName", is(comment.getAuthor().getName())))
                .andExpect(jsonPath("$.created", Matchers.is(comment.getCreated()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @SneakyThrows
    @Test
    void createComment_shouldAnswerBadRequestWhenNameIsEmpty() {
        CommentDto commentDto = new CommentDto("");

        mvc.perform(post("/items/1/comment")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void createComment_shouldAnswerBadRequestWhenNameIsBlank() {
        CommentDto commentDto = new CommentDto("  ");

        mvc.perform(post("/items/1/comment")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
