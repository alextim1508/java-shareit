package ru.practicum.shareit.request.dto;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.ItemRequestBaseTest;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoOutTest extends ItemRequestBaseTest {

    @Autowired
    private JacksonTester<ItemRequestDtoOut> jacksonTester;

    @Test
    void toItemRequestWithAnswersDto() throws IOException {
        JsonContent<ItemRequestDtoOut> result = jacksonTester.write(itemRequestDtoOut);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemRequestDtoOut.getId());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestDtoOut.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(itemRequestDtoOut.getCreated()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathArrayValue("$.items").extracting("id").contains(
                itemDtoOut.getId());
        assertThat(result).extractingJsonPathArrayValue("$.items").extracting("name").contains(
                itemDtoOut.getName());
        assertThat(result).extractingJsonPathArrayValue("$.items").extracting("description").contains(
                itemDtoOut.getDescription());
        assertThat(result).extractingJsonPathArrayValue("$.items").extracting("available").contains(
                itemDtoOut.getAvailable());
    }

    @Test
    void equalsAndHashCodeTest() {
        ItemRequestDtoOut x = ItemRequestDtoOut.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(List.of(itemDtoOut))
                .build();

        ItemRequestDtoOut y = ItemRequestDtoOut.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(List.of(itemDtoOut))
                .build();

        AssertionsForClassTypes.assertThat(x.equals(y) && y.equals(x)).isTrue();
        AssertionsForClassTypes.assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void equalsTest() {
        assertThat(itemRequestDtoOut.equals(itemRequestDtoOut)).isTrue();
        assertThat(itemRequestDtoOut.equals(null)).isFalse();
        assertThat(itemRequestDtoOut.equals(new Object())).isFalse();
    }

    @Test
    void noArgsConstructorTest() {
        ItemRequestDtoOut itemRequestDto = new ItemRequestDtoOut();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setItems(List.of(itemDtoOut));

        assertThat(itemRequestDtoOut.getId()).isEqualTo(itemRequest.getId());
        assertThat(itemRequestDtoOut.getDescription()).isEqualTo(itemRequest.getDescription());
        assertThat(itemRequestDtoOut.getCreated()).isEqualTo(itemRequest.getCreated());
        assertThat(itemRequestDtoOut.getItems()).isEqualTo(List.of(itemDtoOut));
    }

    @Test
    void toStringTest() {
        assertThat(itemRequestDtoOut.toString()).startsWith(itemRequestDtoOut.getClass().getSimpleName());
    }
}
