package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.ItemBaseTest;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemDtoInTest extends ItemBaseTest {

    @Autowired
    private JacksonTester<ItemDtoIn> jacksonTester;

    @Test
    void toItemDto() throws IOException {
        JsonContent<ItemDtoIn> result = jacksonTester.write(itemDtoIn);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDtoIn.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDtoIn.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDtoIn.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(itemDtoIn.getRequestId());
    }

    @Test
    void equalsAndHashCodeTest() {
        ItemDtoIn x = ItemDtoIn.builder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(itemRequest.getId())
                .build();

        ItemDtoIn y = ItemDtoIn.builder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(itemRequest.getId())
                .build();

        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void equalsTest() {
        assertThat(item.equals(item)).isTrue();
        assertThat(item.equals(null)).isFalse();
        assertThat(item.equals(new Object())).isFalse();
    }

    @Test
    void noArgsConstructorTest() {
        ItemDtoIn itemDtoOut = new ItemDtoIn();
        itemDtoOut.setName(item.getName());
        itemDtoOut.setDescription(item.getDescription());
        itemDtoOut.setAvailable(item.getAvailable());
        itemDtoOut.setRequestId(item.getRequest().getId());

        assertThat(itemDtoOut.getName()).isEqualTo(item.getName());
        assertThat(itemDtoOut.getDescription()).isEqualTo(item.getDescription());
        assertThat(itemDtoOut.getAvailable()).isEqualTo(item.getAvailable());
        assertThat(itemDtoOut.getRequestId()).isEqualTo(item.getRequest().getId());
    }

    @Test
    void toStringTest() {
        assertThat(itemDtoIn.toString()).startsWith(itemDtoIn.getClass().getSimpleName());
    }
}
