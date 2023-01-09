package ru.practicum.shareit.request.dto;


import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoInTest {

    @Autowired
    private JacksonTester<ItemRequestDtoIn> jacksonTester;

    ItemRequestDtoIn itemRequestDtoIn;

    @BeforeEach
    protected void setUp() {
        itemRequestDtoIn = new ItemRequestDtoIn("description");
    }

    @Test
    void toItemRequestWithAnswersDto() throws IOException {
        JsonContent<ItemRequestDtoIn> result = jacksonTester.write(itemRequestDtoIn);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestDtoIn.getDescription());
    }

    @Test
    void equalsAndHashCodeTest() {
        ItemRequestDtoIn x = new ItemRequestDtoIn(itemRequestDtoIn.getDescription());

        ItemRequestDtoIn y = new ItemRequestDtoIn(itemRequestDtoIn.getDescription());

        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void equals_shouldReturnFalseWhenDescriptionsAreNotTheSame() {
        ItemRequestDtoIn x = new ItemRequestDtoIn(itemRequestDtoIn.getDescription());

        ItemRequestDtoIn y = new ItemRequestDtoIn("other description");

        AssertionsForClassTypes.assertThat(x.equals(y)).isFalse();
    }

    @Test
    void equalsTest() {
        assertThat(itemRequestDtoIn.equals(itemRequestDtoIn)).isTrue();
        assertThat(itemRequestDtoIn.equals(null)).isFalse();
        assertThat(itemRequestDtoIn.equals(new Object())).isFalse();
    }

    @Test
    void noArgsConstructorTest() {
        ItemRequestDtoIn itemRequestDto = new ItemRequestDtoIn();
        itemRequestDto.setDescription(itemRequestDtoIn.getDescription());

        assertThat(itemRequestDto.getDescription()).isEqualTo(itemRequestDtoIn.getDescription());
    }

    @Test
    void allArgsConstructorTest() {
        ItemRequestDtoIn itemRequestDto = new ItemRequestDtoIn(itemRequestDtoIn.getDescription());
        assertThat(itemRequestDto.getDescription()).isEqualTo(itemRequestDtoIn.getDescription());
    }

    @Test
    void toStringTest() {
        assertThat(itemRequestDtoIn.toString()).startsWith(itemRequestDtoIn.getClass().getSimpleName());
    }
}