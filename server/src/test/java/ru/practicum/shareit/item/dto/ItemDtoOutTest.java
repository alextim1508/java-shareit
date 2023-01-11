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
    void equals_shouldReturnFalseWhenIdsAreNotTheSame() {
        ItemDtoOut x = ItemDtoOut.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(itemRequest.getId())
                .build();

        ItemDtoOut y = ItemDtoOut.builder()
                .id(-1)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(itemRequest.getId())
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseWhenNamesAreNotTheSame() {
        ItemDtoOut x = ItemDtoOut.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(itemRequest.getId())
                .build();

        ItemDtoOut y = ItemDtoOut.builder()
                .id(item.getId())
                .name("other name")
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(itemRequest.getId())
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseWhenDescriptionsAreNotTheSame() {
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
                .description("other description")
                .available(item.getAvailable())
                .requestId(itemRequest.getId())
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseWhenAvailabilitiesAreNotTheSame() {
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
                .available(false)
                .requestId(itemRequest.getId())
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseWhenRequestIdsAreNotTheSame() {
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
                .requestId(-1)
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equalsTest() {
        assertThat(itemDtoOut.equals(itemDtoOut)).isTrue();
        assertThat(itemDtoOut.equals(null)).isFalse();
        assertThat(itemDtoOut.equals(new Object())).isFalse();
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
    void allArgsConstructorTest() {
        ItemDtoOut itemDtoOut = new ItemDtoOut(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest().getId());

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
