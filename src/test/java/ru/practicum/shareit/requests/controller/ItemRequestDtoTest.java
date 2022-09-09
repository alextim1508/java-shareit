package ru.practicum.shareit.requests.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.controller.ItemDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> jacksonTester;


    @Test
    void toItemRequestWithAnswersDto() throws IOException {
        LocalDateTime now = LocalDateTime.now();

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .comments(Collections.emptyList())
                .build();

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("description")
                .items(Arrays.asList(itemDto))
                .created(now)
                .build();

        JsonContent<ItemRequestDto> result = jacksonTester.write(itemRequestDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(now
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathArrayValue("$.items").extracting("id").contains(1);
    }
}