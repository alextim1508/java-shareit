package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentDtoTest {

    @Autowired
    private JacksonTester<CommentDto> jacksonTester;

    CommentDto commentDto;
    LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        commentDto = new CommentDto(1, "comment", "authorName", now);
    }

    @Test
    void toItemDto() throws IOException {
        JsonContent<CommentDto> result = jacksonTester.write(commentDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(commentDto.getId());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentDto.getText());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(commentDto.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(now
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }
}
