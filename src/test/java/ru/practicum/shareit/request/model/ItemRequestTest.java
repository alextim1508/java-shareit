package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.ItemRequestBaseTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRequestTest extends ItemRequestBaseTest {


    @Test
    void equalsTest() {
        ItemRequest x = ItemRequest.builder()
                .id(1)
                .description("description")
                .requestor(requester)
                .created(now)
                .items(List.of(item))
                .build();

        ItemRequest y = ItemRequest.builder()
                .id(1)
                .description("description")
                .requestor(requester)
                .created(now)
                .items(List.of(item))
                .build();

        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void requiredArgsConstructorTest() {
        String description = "description";

        ItemRequest itemRequest = new ItemRequest(description, requester);

        assertThat(itemRequest.getDescription()).isEqualTo(description);
        assertThat(itemRequest.getRequestor()).isEqualTo(requester);
    }

    @Test
    void noArgsConstructorTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(this.itemRequest.getId());
        itemRequest.setDescription(this.itemRequest.getDescription());
        itemRequest.setRequestor(requester);
        itemRequest.setItems(List.of(item));
        itemRequest.onCreate();

        assertThat(itemRequest.getId()).isEqualTo(this.itemRequest.getId());
        assertThat(itemRequest.getDescription()).isEqualTo(this.itemRequest.getDescription());
        assertThat(itemRequest.getRequestor()).isEqualTo(requester);
        assertThat(itemRequest.getCreated()).isEqualTo(now);
        assertThat(itemRequest.getItems()).isEqualTo(List.of(item));
    }

    @Test
    void allArgsConstructorTest() {
        ItemRequest itemRequest = new ItemRequest(
                this.itemRequest.getId(),
                this.itemRequest.getDescription(),
                this.itemRequest.getCreated(),
                requester,
                List.of(item));

        assertThat(itemRequest.getId()).isEqualTo(this.itemRequest.getId());
        assertThat(itemRequest.getDescription()).isEqualTo(this.itemRequest.getDescription());
        assertThat(itemRequest.getRequestor()).isEqualTo(requester);
        assertThat(itemRequest.getCreated()).isEqualTo(this.itemRequest.getCreated());
        assertThat(itemRequest.getItems()).isEqualTo(List.of(item));
    }


    @Test
    void toStringTest() {
        String description = "description";
        ItemRequest itemRequest = new ItemRequest(description, requester);

        assertThat(itemRequest.toString()).startsWith(itemRequest.getClass().getSimpleName());
    }
}
