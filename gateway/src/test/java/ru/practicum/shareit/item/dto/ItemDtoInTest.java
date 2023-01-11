package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemDtoInTest {

    @Autowired
    private JacksonTester<ItemDtoIn> jacksonTester;

    ItemDtoIn itemDtoIn;


    @BeforeEach
    protected void setUp() {
        itemDtoIn = ItemDtoIn.builder()
                .name("name")
                .description("description")
                .available(true)
                .requestId(1)
                .build();
    }

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
                .name(itemDtoIn.getName())
                .description(itemDtoIn.getDescription())
                .available(itemDtoIn.getAvailable())
                .requestId(itemDtoIn.getRequestId())
                .build();

        ItemDtoIn y = ItemDtoIn.builder()
                .name(itemDtoIn.getName())
                .description(itemDtoIn.getDescription())
                .available(itemDtoIn.getAvailable())
                .requestId(itemDtoIn.getRequestId())
                .build();

        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void equals_shouldReturnFalseWhenNamesAreNotTheSame() {
        ItemDtoIn x = ItemDtoIn.builder()
                .name(itemDtoIn.getName())
                .description(itemDtoIn.getDescription())
                .available(itemDtoIn.getAvailable())
                .requestId(itemDtoIn.getRequestId())
                .build();

        ItemDtoIn y = ItemDtoIn.builder()
                .name("other name")
                .description(itemDtoIn.getDescription())
                .available(itemDtoIn.getAvailable())
                .requestId(itemDtoIn.getRequestId())
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseWhenDescriptionsAreNotTheSame() {
        ItemDtoIn x = ItemDtoIn.builder()
                .name(itemDtoIn.getName())
                .description(itemDtoIn.getDescription())
                .available(itemDtoIn.getAvailable())
                .requestId(itemDtoIn.getRequestId())
                .build();

        ItemDtoIn y = ItemDtoIn.builder()
                .name(itemDtoIn.getName())
                .description("other description")
                .available(itemDtoIn.getAvailable())
                .requestId(itemDtoIn.getRequestId())
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseWhenAvailabilitiesAreNotTheSame() {
        ItemDtoIn x = ItemDtoIn.builder()
                .name(itemDtoIn.getName())
                .description(itemDtoIn.getDescription())
                .available(itemDtoIn.getAvailable())
                .requestId(itemDtoIn.getRequestId())
                .build();

        ItemDtoIn y = ItemDtoIn.builder()
                .name(itemDtoIn.getName())
                .description(itemDtoIn.getDescription())
                .available(false)
                .requestId(itemDtoIn.getRequestId())
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equals_shouldReturnFalseWhenRequestIdsAreNotTheSame() {
        ItemDtoIn x = ItemDtoIn.builder()
                .name(itemDtoIn.getName())
                .description(itemDtoIn.getDescription())
                .available(itemDtoIn.getAvailable())
                .requestId(itemDtoIn.getRequestId())
                .build();

        ItemDtoIn y = ItemDtoIn.builder()
                .name(itemDtoIn.getName())
                .description(itemDtoIn.getDescription())
                .available(itemDtoIn.getAvailable())
                .requestId(-1)
                .build();

        assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equalsTest() {
        assertThat(itemDtoIn.equals(itemDtoIn)).isTrue();
        assertThat(itemDtoIn.equals(null)).isFalse();
        assertThat(itemDtoIn.equals(new Object())).isFalse();
    }

    @Test
    void noArgsConstructorTest() {
        ItemDtoIn itemDtoOut = new ItemDtoIn();
        itemDtoOut.setName(itemDtoIn.getName());
        itemDtoOut.setDescription(itemDtoIn.getDescription());
        itemDtoOut.setAvailable(itemDtoIn.getAvailable());
        itemDtoOut.setRequestId(itemDtoIn.getRequestId());

        assertThat(itemDtoOut.getName()).isEqualTo(itemDtoIn.getName());
        assertThat(itemDtoOut.getDescription()).isEqualTo(itemDtoIn.getDescription());
        assertThat(itemDtoOut.getAvailable()).isEqualTo(itemDtoIn.getAvailable());
        assertThat(itemDtoOut.getRequestId()).isEqualTo(itemDtoIn.getRequestId());
    }

    @Test
    void allArgsConstructorTest() {
        ItemDtoIn itemDtoOut = new ItemDtoIn(
                itemDtoIn.getName(),
                itemDtoIn.getDescription(),
                itemDtoIn.getAvailable(),
                itemDtoIn.getRequestId());

        assertThat(itemDtoOut.getName()).isEqualTo(itemDtoIn.getName());
        assertThat(itemDtoOut.getDescription()).isEqualTo(itemDtoIn.getDescription());
        assertThat(itemDtoOut.getAvailable()).isEqualTo(itemDtoIn.getAvailable());
        assertThat(itemDtoOut.getRequestId()).isEqualTo(itemDtoIn.getRequestId());
    }

    @Test
    void toStringTest() {
        assertThat(itemDtoIn.toString()).startsWith(itemDtoIn.getClass().getSimpleName());
    }
}
