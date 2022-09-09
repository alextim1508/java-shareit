package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ShortItemDtoTest {

    @Autowired
    private JacksonTester<ShortItemDto> jacksonTester;

    ShortItemDto shortItemDto;

    @BeforeEach
    void setUp() {
        this.shortItemDto = new ShortItemDto(1, "name", 2);
    }

    @Test
    void toItemDto() throws IOException {
        JsonContent<ShortItemDto> result = jacksonTester.write(shortItemDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(shortItemDto.getId());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(shortItemDto.getName());
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(shortItemDto.getOwnerId());
    }
}
