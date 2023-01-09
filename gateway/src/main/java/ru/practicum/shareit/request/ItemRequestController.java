package ru.practicum.shareit.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Map;

import static ru.practicum.shareit.util.Util.DEFAULT_PAGE_SIZE;
import static ru.practicum.shareit.util.Util.USER_ID_HEADER;

@RestController
@RequestMapping(path = "/requests", produces = "application/json")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestClient client;

    private final ObjectMapper objectMapper;

    @PostMapping
    public Mono<ResponseEntity<String>> create(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @Valid @RequestBody ItemRequestDtoIn itemRequestDto) throws JsonProcessingException {

        return client.post("/", userId, objectMapper.writeValueAsString(itemRequestDto));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<String>> getById(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @PathVariable Integer id) {

        return client.get(String.format("/%d", id), userId, null);
    }

    @GetMapping
    public Mono<ResponseEntity<String>> getAllByRequestor(
            @RequestHeader(USER_ID_HEADER) Integer userId) {

        return client.get("/", userId, null);
    }

    @GetMapping("all")
    public Mono<ResponseEntity<String>> getAll(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @Min(0) @RequestParam(required = false, defaultValue = "0") Integer from,
            @Min(1) @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {

        return client.get("/all", userId, Map.of("from", from, "size", size));
    }
}
