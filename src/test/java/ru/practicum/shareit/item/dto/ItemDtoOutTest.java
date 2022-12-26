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
public class ItemDtoOutTest extends ItemBaseTest {

    @Autowired
    private JacksonTester<ItemDtoOut> jacksonTester;

    @Test
    void toItemDto() throws IOException {
        JsonContent<ItemDtoOut> result = jacksonTester.write(itemDtoOut);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemDtoOut.getId());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDtoOut.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDtoOut.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDtoOut.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(itemDtoOut.getRequestId());
    }

    @Test
    void equalsAndHashCodeTest() {
        ItemDtoOut x = ItemDtoOut.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(itemRequest.getId())
                .build();

        ItemDtoOut y = ItemDtoOut.builder()
                .id(item.getId())
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
        ItemDtoOut x = ItemDtoOut.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(itemRequest.getId())
                .build();

        assertThat(x.equals(x)).isTrue();
        assertThat(x.equals(null)).isFalse();
        assertThat(x.equals(new Object())).isFalse();

    }

    @Test
    void noArgsConstructorTest() {
        ItemDtoOut itemDtoOut = new ItemDtoOut();
        itemDtoOut.setId(item.getId());
        itemDtoOut.setName(item.getName());
        itemDtoOut.setDescription(item.getDescription());
        itemDtoOut.setAvailable(item.getAvailable());
        itemDtoOut.setRequestId(item.getRequest().getId());

        assertThat(itemDtoOut.getId()).isEqualTo(item.getId());
        assertThat(itemDtoOut.getName()).isEqualTo(item.getName());
        assertThat(itemDtoOut.getDescription()).isEqualTo(item.getDescription());
        assertThat(itemDtoOut.getAvailable()).isEqualTo(item.getAvailable());
        assertThat(itemDtoOut.getRequestId()).isEqualTo(item.getRequest().getId());
    }

    @Test
    void toStringTest() {
        assertThat(itemDtoOut.toString()).startsWith(itemDtoOut.getClass().getSimpleName());
    }

}
