package ru.practicum.shareit.requests.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRequestTest {

    User owner, requester;
    Item item;
    LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now().withNano(0);

        owner = new User(1, "owner", "owner@gmail.com");
        item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .comments(Collections.emptyList())
                .build();

        requester = new User(2, "requester", "requester@gmail.com");
    }

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
        int id = 1;
        String description = "description";

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(id);
        itemRequest.setDescription(description);
        itemRequest.setRequestor(requester);
        itemRequest.setItems(List.of(item));
        itemRequest.onCreate();

        assertThat(itemRequest.getId()).isEqualTo(id);
        assertThat(itemRequest.getDescription()).isEqualTo(description);
        assertThat(itemRequest.getRequestor()).isEqualTo(requester);
        assertThat(itemRequest.getCreated()).isEqualTo(now);
        assertThat(itemRequest.getItems()).isEqualTo(List.of(item));
    }

    @Test
    void toStringTest() {
        String description = "description";
        ItemRequest itemRequest = new ItemRequest(description, requester);

        assertThat(itemRequest.toString()).startsWith(itemRequest.getClass().getSimpleName());
    }
}
