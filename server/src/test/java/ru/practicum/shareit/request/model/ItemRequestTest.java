package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.ItemRequestBaseTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ItemRequestTest extends ItemRequestBaseTest {


    @Test
    void equalsAndHashCodeTest() {
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
    void equalsTest() {
        assertThat(itemRequest.equals(itemRequest)).isTrue();
        assertThat(itemRequest.equals(null)).isFalse();
        assertThat(itemRequest.equals(new Object())).isFalse();
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
    void allArgsConstructor_shouldThrowNotFoundExceptionWheDescriptionIsNull() {
        assertThatThrownBy(() -> {
            ItemRequest itemRequest = new ItemRequest(
                    this.itemRequest.getId(),
                    null,
                    this.itemRequest.getCreated(),
                    requester,
                    List.of(item));
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("description is marked non-null but is null");
    }

    @Test
    void allArgsConstructor_shouldThrowNotFoundExceptionWheRequestorIsNull() {
        assertThatThrownBy(() -> {
            ItemRequest itemRequest = new ItemRequest(
                    this.itemRequest.getId(),
                    this.itemRequest.getDescription(),
                    this.itemRequest.getCreated(),
                    null,
                    List.of(item));
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("requestor is marked non-null but is null");
    }

    @Test
    void builder_shouldReturnNotNull() {
        ItemRequest.ItemRequestBuilder builder = ItemRequest.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(requester)
                .created(now)
                .items(List.of(item));

        assertThat(builder.toString()).contains(builder.getClass().getSimpleName());
        assertThat(builder.build()).isNotNull();
    }

    @Test
    void builder_shouldThrowNotFoundExceptionWhenDescriptionIsNull() {
        assertThatThrownBy(() -> {
            ItemRequest.builder()
                    .id(itemRequest.getId())
                    .description(null)
                    .requestor(requester)
                    .created(now)
                    .items(List.of(item))
                    .build();

        }).isInstanceOf(NullPointerException.class)
          .hasMessage("description is marked non-null but is null");
    }

    @Test
    void builder_shouldThrowNotFoundExceptionWhenRequestorIsNull() {
        assertThatThrownBy(() -> {
            ItemRequest.builder()
                    .id(itemRequest.getId())
                    .description(itemRequest.getDescription())
                    .requestor(null)
                    .created(now)
                    .items(List.of(item))
                    .build();

        }).isInstanceOf(NullPointerException.class)
          .hasMessage("requestor is marked non-null but is null");
    }

    @Test
    void setDescription_shouldThrowNotFoundExceptionWhenDescriptionIsNull() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(this.itemRequest.getId())
                .description(this.itemRequest.getDescription())
                .requestor(requester)
                .created(now)
                .items(List.of(item))
                .build();

        assertThatThrownBy(() -> {
            itemRequest.setDescription(null);
        }).isInstanceOf(NullPointerException.class)
          .hasMessage("description is marked non-null but is null");
    }

    @Test
    void setRequestor_shouldThrowNotFoundExceptionWhenDescriptionIsNull() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(this.itemRequest.getId())
                .description(this.itemRequest.getDescription())
                .requestor(requester)
                .created(now)
                .items(List.of(item))
                .build();

        assertThatThrownBy(() -> {
            itemRequest.setRequestor(null);
        }).isInstanceOf(NullPointerException.class)
          .hasMessage("requestor is marked non-null but is null");
    }

    @Test
    void toStringTest() {
        assertThat(itemRequest.toString()).startsWith(itemRequest.getClass().getSimpleName());
    }
}
