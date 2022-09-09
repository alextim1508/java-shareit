package ru.practicum.shareit.item.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.controller.ShortBookingDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> jacksonTester;

    ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .build();
    }

    @Test
    void toItemDto() throws IOException {
        JsonContent<ItemDto> result = jacksonTester.write(itemDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemDto.getId());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDto.getAvailable());
    }

    @Test
    void toItemWithShortBookingDto() throws IOException {
        LocalDateTime now = LocalDateTime.now();

        ShortBookingDto lastBooking = new ShortBookingDto(1, 1, now.minusDays(2), now.minusDays(1));
        ShortBookingDto nextBooking = new ShortBookingDto(2, 1, now.plusDays(1), now.plusDays(2));
        CommentDto commentDto = new CommentDto(1, "comment", "user", now.plusDays(3));

        itemDto.setNextBooking(nextBooking);
        itemDto.setLastBooking(lastBooking);
        itemDto.setRequestId(1);
        itemDto.setComments(List.of(commentDto));

        JsonContent<ItemDto> result = jacksonTester.write(itemDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemDto.getId());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(itemDto.getRequestId());
        assertThat(result).extractingJsonPathArrayValue("$.comments").extracting("id")
                .contains(itemDto.getComments().get(0).getId());
        assertThat(result).extractingJsonPathArrayValue("$.comments").extracting("text")
                .contains(itemDto.getComments().get(0).getText());
        assertThat(result).extractingJsonPathArrayValue("$.comments").extracting("authorName")
                .contains(itemDto.getComments().get(0).getAuthorName());
        assertThat(result).extractingJsonPathArrayValue("$.comments").extracting("created")
                .contains(itemDto.getComments().get(0).getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id")
                .isEqualTo(itemDto.getNextBooking().getId());
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId")
                .isEqualTo(itemDto.getNextBooking().getBookerId());
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.start")
                .isEqualTo(itemDto.getNextBooking().getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.end")
                .isEqualTo(itemDto.getNextBooking().getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id")
                .isEqualTo(itemDto.getLastBooking().getId());
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId")
                .isEqualTo(itemDto.getLastBooking().getBookerId());
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.start")
                .isEqualTo(itemDto.getLastBooking().getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.end")
                .isEqualTo(itemDto.getLastBooking().getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }
}