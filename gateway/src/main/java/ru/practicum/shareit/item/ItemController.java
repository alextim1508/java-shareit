package ru.practicum.shareit.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.util.validation.Create;
import ru.practicum.shareit.util.validation.Update;

import javax.validation.Valid;
import java.util.Map;

import static ru.practicum.shareit.util.Util.USER_ID_HEADER;


@RestController
@RequestMapping(path = "/items", produces = "application/json")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient client;

    private final ObjectMapper objectMapper;

    @PostMapping
    public Mono<ResponseEntity<String>> create(
            @RequestHeader(USER_ID_HEADER) Integer ownerId,
            @Validated(Create.class) @RequestBody ItemDtoIn itemDto) throws JsonProcessingException {

        return client.post("/", ownerId, objectMapper.writeValueAsString(itemDto));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<String>> getById(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @PathVariable Integer id) {

        return client.get(String.format("/%d", id), userId, null);
    }

    @GetMapping
    public Mono<ResponseEntity<String>> getAll(
            @RequestHeader(USER_ID_HEADER) Integer userId) {

        return client.get("/", userId, null);
    }

    @GetMapping("/search")
    public Mono<ResponseEntity<String>> searchByKeyword(
            @RequestParam(required = false, defaultValue = "") String text) {

        if (text.isEmpty()) {
            return Mono.just(ResponseEntity.ok().body("[]"));
        }
        return client.get("/search", null, Map.of("text", text));
    }

   @PatchMapping("/{id}")
    public Mono<ResponseEntity<String>> update(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @Validated(Update.class) @RequestBody ItemDtoIn itemDto,
            @PathVariable Integer id) throws JsonProcessingException {

        return client.patch(String.format("/%d", id), userId, objectMapper.writeValueAsString(itemDto));
    }

    @PostMapping("/{id}/comment")
    public Mono<ResponseEntity<String>> createComment(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @Valid @RequestBody CommentDtoIn commentDto,
            @PathVariable Integer id) throws JsonProcessingException {

        return client.post(String.format("/%d/comment", id), userId, objectMapper.writeValueAsString(commentDto));
    }
}
