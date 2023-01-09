package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.util.ErrorHandler;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.Util.USER_ID_HEADER;

@SpringBootTest
public class BookingControllerTest {

    @Autowired
    private BookingController controller;

    @MockBean
    private BookingClient client;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    LocalDateTime now;
    BookingDtoIn bookingDtoIn;

    @BeforeEach
    protected void setUp() {
        mapper.registerModule(new JavaTimeModule());

        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ErrorHandler.class)
                .build();

        now = LocalDateTime.now();
        bookingDtoIn = BookingDtoIn.builder()
                .itemId(1)
                .startDate(now.plusDays(1))
                .endDate(now.plusDays(2))
                .build();
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerOK() {
        when(client.post(anyString(), anyInt(), anyString()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(bookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenStartAndEndInPast() {
        bookingDtoIn.setStartDate(now.minusDays(2));
        bookingDtoIn.setEndDate(now.minusDays(1));

        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(bookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenStartInPast() {
        bookingDtoIn.setStartDate(now.minusDays(2));
        bookingDtoIn.setEndDate(now.plusDays(1));

        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(bookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenStartAndEndAreMixedUp() {
        bookingDtoIn.setStartDate(now.plusDays(2));
        bookingDtoIn.setEndDate(now.minusDays(1));

        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .content(mapper.writeValueAsString(bookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void create_shouldAnswerBadRequestWhenUserIdIsMissing() {
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoIn))
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

        mvc.perform(get("/bookings/1")
                        .header(USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
    void getAllByBooker_shouldAnswerOK() {
        when(client.get(anyString(), anyInt(), anyMap()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .param("from", "0")
                        .param("size", "10")
                        .param("state", "ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAllByBooker_shouldAnswerOKWhenFromParamIsMissing() {
        when(client.get(anyString(), anyInt(), anyMap()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .param("size", "10")
                        .param("state", "ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAllByBooker_shouldAnswerOKWhenSizeParamIsMissing() {
        when(client.get(anyString(), anyInt(), anyMap()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .param("from", "0")
                        .param("state", "ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAllByBooker_shouldAnswerOKWhenStateParamIsMissing() {
        when(client.get(anyString(), anyInt(), anyMap()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/bookings")
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
    void getAllByBooker_shouldAnswerBadRequestWhenUserIdIsMissing() {
        when(client.get(anyString(), anyInt(), anyMap()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/bookings")
                        .param("from", "0")
                        .param("size", "10")
                        .param("state", "ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldAnswerOK() {
        when(client.get(anyString(), anyInt(), anyMap()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, "1")
                        .param("from", "0")
                        .param("size", "10")
                        .param("state", "ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldAnswerOKWhenFromParamIsMissing() {
        when(client.get(anyString(), anyInt(), anyMap()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, "1")
                        .param("size", "10")
                        .param("state", "ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldAnswerOKWhenSizeParamIsMissing() {
        when(client.get(anyString(), anyInt(), anyMap()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, "1")
                        .param("from", "0")
                        .param("state", "ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getAllByOwner_shouldAnswerOKWhenStateParamIsMissing() {
        when(client.get(anyString(), anyInt(), anyMap()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/bookings/owner")
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
    void getAllByOwner_shouldAnswerBadRequestWhenUserIdIsMissing() {
        when(client.get(anyString(), anyInt(), anyMap()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(get("/bookings/owner")
                        .param("from", "0")
                        .param("size", "10")
                        .param("state", "ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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
    void setApproved_shouldAnswerBadRequestWhenParamApproveIsMissing() {
        mvc.perform(patch("/bookings/1")
                        .header(USER_ID_HEADER, "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @SneakyThrows
    @Test
    void setApproved_shouldAnswerOK() {
        when(client.patch(anyString(), anyInt(), anyMap()))
                .thenReturn(Mono.just(ResponseEntity.ok().body("OK")));

        mvc.perform(patch("/bookings/1")
                        .header(USER_ID_HEADER, "1")
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
