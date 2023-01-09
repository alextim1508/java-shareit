package ru.practicum.shareit.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDtoIn;
import ru.practicum.shareit.util.validation.Create;
import ru.practicum.shareit.util.validation.Update;

@RestController
@RequestMapping(path = "/users", produces = "application/json")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserClient client;

    private final ObjectMapper objectMapper;

    @PostMapping
    public Mono<ResponseEntity<String>> create(
            @Validated(Create.class) @RequestBody UserDtoIn userDto) throws JsonProcessingException {

        return client.post(objectMapper.writeValueAsString(userDto));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<String>> getById(
            @PathVariable Integer id) {

        return client.get(String.valueOf(id));
    }

    @GetMapping
    public Mono<ResponseEntity<String>> getAll() {

        return client.get();
    }

    @PatchMapping({"/{id}"})
    public Mono<ResponseEntity<String>> update(
            @Validated(Update.class) @RequestBody UserDtoIn userDto,
            @PathVariable Integer id) throws JsonProcessingException {

        return client.patch(String.valueOf(id), objectMapper.writeValueAsString(userDto));
    }

    @DeleteMapping({"/{id}"})
    public Mono<ResponseEntity<String>> delete(
            @PathVariable Integer id) {

        return client.delete(String.valueOf(id));
    }
}
