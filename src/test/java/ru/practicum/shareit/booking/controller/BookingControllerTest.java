package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.controller.BookingInputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemFactory;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.ErrorHandler;
import ru.practicum.shareit.util.exception.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;
import static ru.practicum.shareit.user.controller.UserController.USER_ID_HEADER;

@SpringBootTest
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @MockBean
    private UserService userService;

    @Autowired
    private BookingController controller;

    @MockBean
    private ItemFactory itemFactory;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());

        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ErrorHandler.class)
                .build();
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerOK() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = Booking.builder()
                .id(1)
                .startDate(start)
                .endDate(end)
                .item(item)
                .booker(user)
                .status(WAITING)
                .build();

        BookingInputDto bookingDto = new BookingInputDto(
                item.getId(),
                start,
                end);

        when(bookingService.create(any(Booking.class)))
                .thenReturn(booking);

        when(userService.getById(1))
                .thenReturn(user);
        when(userService.getById(2))
                .thenReturn(owner);

        when(itemFactory.get(anyInt()))
                .thenReturn(item);


        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println("response = " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$.start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$.item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$.item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$.item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$.item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$.item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$.item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$.item.comments", Matchers.empty()))
                .andExpect(jsonPath("$.booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$.booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenStartAndEndInPast() {
        BookingInputDto bookingDto = new BookingInputDto(
                1,
                LocalDateTime.now().minusSeconds(2),
                LocalDateTime.now().minusSeconds(2)
        );

        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenStartInPast() {
        BookingInputDto bookingDto = new BookingInputDto(
                1,
                LocalDateTime.now().minusSeconds(2),
                LocalDateTime.now().plusSeconds(2)
        );

        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenEndInPast() {
        BookingInputDto bookingDto = new BookingInputDto(
                1,
                LocalDateTime.now().plusSeconds(2),
                LocalDateTime.now().minusDays(2)
        );

        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenStartAndEndAreMixedUp() {
        BookingInputDto bookingDto = new BookingInputDto(
                1,
                LocalDateTime.now().plusSeconds(3),
                LocalDateTime.now().plusSeconds(2)
        );

        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenUserIdIsMissing() {
        BookingInputDto bookingDto = new BookingInputDto(
                1,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2)
        );

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenItemIsNotAvailable() {
        BookingInputDto bookingDto = new BookingInputDto(
                1,
                LocalDateTime.now().plusSeconds(3),
                LocalDateTime.now().plusSeconds(2)
        );

        when(bookingService.create(any(Booking.class)))
                .thenThrow(new ItemIsNotAvailableException());

        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenItemDoesNotFounded() {
        BookingInputDto bookingDto = new BookingInputDto(
                1,
                LocalDateTime.now().plusSeconds(3),
                LocalDateTime.now().plusSeconds(2)
        );

        when(bookingService.create(any(Booking.class)))
                .thenThrow(new NotFoundException(Booking.class));

        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getById_shouldAnswerOK() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = Booking.builder()
                .id(1)
                .startDate(start)
                .endDate(end)
                .item(item)
                .booker(user)
                .status(WAITING)
                .build();


        when(bookingService.getById(anyInt(), anyInt()))
                .thenReturn(booking);

        mvc.perform(get("/bookings/1")
                        .header(USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println("response = " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$.start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$.item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$.item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$.item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$.item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$.item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$.item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$.item.comments", Matchers.empty()))
                .andExpect(jsonPath("$.booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$.booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void getById_shouldAnswerBadRequestWhenUserIdIsMissing() {
        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getById_shouldAnswerNotFoundWhenItemIsNotFounded() {
        when(bookingService.getById(anyInt(), anyInt()))
                .thenThrow(new NotFoundException(""));

        mvc.perform(get("/bookings/1")
                        .header(USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void getById_shouldAnswerNotFoundRequestWhenActionIsForbidden() {
        BookingInputDto bookingDto = new BookingInputDto(
                1,
                LocalDateTime.now().plusSeconds(3),
                LocalDateTime.now().plusSeconds(2)
        );

        when(bookingService.getById(anyInt(), anyInt()))
                .thenThrow(new ForbiddenException());

        mvc.perform(get("/bookings/1")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void getAllByBooker_shouldAnswerOK() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = new Booking(
                1,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        when(bookingService.getAllByBooker(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking));

        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .param("from", "0")
                        .param("size", "10")
                        .param("state", "ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$[0].start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.comments", Matchers.empty()))
                .andExpect(jsonPath("$[0].booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void getAllByBooker_shouldReturnBadWhenStatusIsUnsupported() {
        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "UNKNOWN")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllByBooker_shouldAnswerOKWhenStateIsMissing() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = new Booking(
                1,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        when(bookingService.getAllByBooker(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking));

        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$[0].start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.comments", Matchers.empty()))
                .andExpect(jsonPath("$[0].booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void getAllByBooker_shouldAnswerOKAndCurrentBooking() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = new Booking(
                1,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        when(bookingService.getCurrentBookingByBooker(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking));

        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "CURRENT")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println("response = " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$[0].start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.comments", Matchers.empty()))
                .andExpect(jsonPath("$[0].booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void getAllByBooker_shouldAnswerOKAndPastBooking() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = new Booking(
                1,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        when(bookingService.getPastBookingByBooker(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking));

        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "PAST")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println("response = " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$[0].start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.comments", Matchers.empty()))
                .andExpect(jsonPath("$[0].booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void getAllByBooker_shouldAnswerOKAndFutureBooking() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = new Booking(
                1,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        when(bookingService.getFutureBookingByBooker(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking));

        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "FUTURE")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println("response = " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$[0].start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.comments", Matchers.empty()))
                .andExpect(jsonPath("$[0].booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void getAllByBooker_shouldAnswerOKAnWaitingBooking() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = new Booking(
                1,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        when(bookingService.getBookingByBookerAndStatus(anyInt(), eq(BookingStatus.WAITING), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking));

        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "WAITING")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println("response = " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$[0].start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.comments", Matchers.empty()))
                .andExpect(jsonPath("$[0].booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void getAllByBooker_shouldAnswerOKAnRejectedBooking() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = new Booking(
                1,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        when(bookingService.getBookingByBookerAndStatus(anyInt(), eq(BookingStatus.REJECTED), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking));

        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "REJECTED")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println("response = " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$[0].start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.comments", Matchers.empty()))
                .andExpect(jsonPath("$[0].booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void getAllByBooker_shouldAnswerBadRequestWhenUserIdIsMissing() {
        mvc.perform(get("/bookings")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllByBooker_shouldAnswerBadRequestWhenParamFromIsNegative() {
        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "REJECTED")
                        .param("from", "-1")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllByBooker_shouldAnswerBadRequestWhenParamToIsZero() {
        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "REJECTED")
                        .param("from", "0")
                        .param("size", "0")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllByBooker_shouldAnswerBadRequestWhenParamToIsNegative() {
        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "REJECTED")
                        .param("from", "0")
                        .param("size", "-1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldAnswerOK() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = new Booking(
                1,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        when(bookingService.getAllByOwner(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking));

        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, "1")
                        .param("from", "0")
                        .param("size", "10")
                        .param("state", "ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$[0].start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.comments", Matchers.empty()))
                .andExpect(jsonPath("$[0].booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldReturnBadWhenStatusIsUnsupported() {
        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "UNKNOWN")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldAnswerOKWhenStateIsMissing() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = new Booking(
                1,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        when(bookingService.getAllByOwner(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking));

        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, "1")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$[0].start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.comments", Matchers.empty()))
                .andExpect(jsonPath("$[0].booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldAnswerOKAndCurrentBooking() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = new Booking(
                1,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        when(bookingService.getCurrentBookingByOwner(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking));

        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "CURRENT")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$[0].start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.comments", Matchers.empty()))
                .andExpect(jsonPath("$[0].booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldAnswerOKAndPastBooking() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = new Booking(
                1,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        when(bookingService.getPastBookingByOwner(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking));

        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "PAST")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$[0].start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.comments", Matchers.empty()))
                .andExpect(jsonPath("$[0].booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldAnswerOKAndFutureBooking() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = new Booking(
                1,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        when(bookingService.getFutureBookingByOwner(anyInt(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking));

        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "FUTURE")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$[0].start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.comments", Matchers.empty()))
                .andExpect(jsonPath("$[0].booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldAnswerOKAnWaitingBooking() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = new Booking(
                1,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        when(bookingService.getBookingByOwnerAndStatus(anyInt(), eq(BookingStatus.WAITING), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking));

        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "WAITING")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println("response = " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$[0].start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.comments", Matchers.empty()))
                .andExpect(jsonPath("$[0].booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldAnswerOKAnRejectedBooking() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        BookingInputDto bookingDto = new BookingInputDto(
                1,
                start,
                end);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = new Booking(
                1,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        when(bookingService.getBookingByOwnerAndStatus(anyInt(), eq(BookingStatus.REJECTED), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(booking));

        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "REJECTED")
                        .param("from", "0")
                        .param("size", "10")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$[0].start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$[0].item.comments", Matchers.empty()))
                .andExpect(jsonPath("$[0].booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$[0].booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldAnswerBadRequestWhenUserIdIsMissing() {
        mvc.perform(get("/bookings/owner")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldAnswerBadRequestWhenParamFromIsNegative() {
        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "REJECTED")
                        .param("from", "-1")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldAnswerBadRequestWhenParamToIsZero() {
        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "REJECTED")
                        .param("from", "0")
                        .param("size", "0")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldAnswerBadRequestWhenParamToIsNegative() {
        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "REJECTED")
                        .param("from", "0")
                        .param("size", "-1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void setApproved_shouldAnswerOK() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        User user = new User(1, "name", "user@gmail.com");
        User owner = new User(2, "name", "owner@gmail.com");
        Item item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        Booking booking = new Booking(
                1,
                start,
                end,
                item,
                user,
                BookingStatus.WAITING);

        when(bookingService.approve(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(booking);

        mvc.perform(patch("/bookings/1")
                        .header(USER_ID_HEADER, "1")
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(booking.getId())))
                .andExpect(jsonPath("$.start", Matchers.is(booking.getStartDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.end", Matchers.is(booking.getEndDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.status", Matchers.is(booking.getStatus().name())))
                .andExpect(jsonPath("$.item.id", Matchers.is(booking.getItem().getId())))
                .andExpect(jsonPath("$.item.name", Matchers.is(booking.getItem().getName())))
                .andExpect(jsonPath("$.item.description", Matchers.is(booking.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", Matchers.is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$.item.requestId").value(Matchers.nullValue()))
                .andExpect(jsonPath("$.item.nextBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$.item.lastBooking").value(Matchers.nullValue()))
                .andExpect(jsonPath("$.item.comments", Matchers.empty()))
                .andExpect(jsonPath("$.booker.id", Matchers.is(booking.getBooker().getId())))
                .andExpect(jsonPath("$.booker.name", Matchers.is(booking.getBooker().getName())))
                .andExpect(jsonPath("$.booker.email", Matchers.is(booking.getBooker().getEmail())));
    }

    @SneakyThrows
    @Test
    void setApproved_shouldAnswerBadRequestWhenUserIdIsMissing() {
        mvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void setApproved_shouldAnswerBadRequestWhenParamApprovedIsMissing() {
        mvc.perform(patch("/bookings/1")
                        .header(USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void approve_shouldAnswerNotFoundRequestWhenActionIsForbidden() {
        BookingInputDto bookingDto = new BookingInputDto(
                1,
                LocalDateTime.now().plusSeconds(3),
                LocalDateTime.now().plusSeconds(2)
        );

        when(bookingService.approve(anyInt(), anyInt(),anyBoolean()))
                .thenThrow(new ForbiddenException());

        mvc.perform(patch("/bookings/1")
                        .header(USER_ID_HEADER, "1")
                        .param("approved", "true")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void approve_shouldAnswerNotFoundRequestWhenItemStatusIsNotWaiting() {
        BookingInputDto bookingDto = new BookingInputDto(
                1,
                LocalDateTime.now().plusSeconds(3),
                LocalDateTime.now().plusSeconds(2)
        );

        when(bookingService.approve(anyInt(), anyInt(),anyBoolean()))
                .thenThrow(new StatusChangeException());

        mvc.perform(patch("/bookings/1")
                        .header(USER_ID_HEADER, "1")
                        .param("approved", "true")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void delete_shouldAnswerOK() {
        mvc.perform(delete("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void delete_shouldBadRequestWhenUserIsNotFound() {
        doThrow(new NotFoundException("")).when(bookingService).delete(anyInt());

        mvc.perform(delete("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
