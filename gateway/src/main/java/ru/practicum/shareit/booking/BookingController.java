package ru.practicum.shareit.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDtoIn;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Map;

import static ru.practicum.shareit.util.Util.DEFAULT_PAGE_SIZE;
import static ru.practicum.shareit.util.Util.USER_ID_HEADER;

@RestController
@RequestMapping(path = "/bookings", produces = "application/json")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient client;

    private final ObjectMapper objectMapper;

    @PostMapping
    public Mono<ResponseEntity<String>> create(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @Valid @RequestBody BookingDtoIn bookingDto) throws JsonProcessingException {

        return client.post("/", userId, objectMapper.writeValueAsString(bookingDto));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<String>> getById(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @PathVariable Integer id) {

        return client.get(String.format("/%d", id), userId, null);
    }


    @GetMapping
    public Mono<ResponseEntity<String>> getAllByBooker(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @RequestParam(defaultValue = "ALL", required = false) String state,
            @Min(0) @RequestParam(required = false, defaultValue = "0") Integer from,
            @Min(1) @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {

        return client.get("/", userId, Map.of("from", from, "size", size, "state", state));
    }

    @GetMapping("/owner")
    public Mono<ResponseEntity<String>> getAllByOwner(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @Min(0) @RequestParam(required = false, defaultValue = "0") Integer from,
            @Min(1) @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {

        return client.get("/owner", userId, Map.of("from", from, "size", size, "state", state));
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<String>> approve(@RequestHeader(USER_ID_HEADER) Integer userId,
                                                @PathVariable Integer id,
                                                @RequestParam Boolean approved) {

        return client.patch(String.format("/%d", id), userId, Map.of("approved", approved));
    }
}
