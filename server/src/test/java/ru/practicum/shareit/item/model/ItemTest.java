package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemBaseTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemTest extends ItemBaseTest {

    @Test
    void equalsAndHashCodeTest() {
        Item x = Item.builder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .build();

        Item y = Item.builder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
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
        Item item = new Item();
        item.setId(this.item.getId());
        item.setName(this.item.getName());
        item.setDescription(this.item.getDescription());
        item.setAvailable(this.item.getAvailable());
        item.setOwner(this.item.getOwner());
        item.setLastBooking(booking);
        item.setNextBooking(booking);
        item.setBookings(List.of(booking));
        item.setRequest(itemRequest);
        item.setComments(List.of(comment));

        assertThat(item.getId()).isEqualTo(this.item.getId());
        assertThat(item.getName()).isEqualTo(this.item.getName());
        assertThat(item.getDescription()).isEqualTo(this.item.getDescription());
        assertThat(item.getAvailable()).isEqualTo(this.item.getAvailable());
        assertThat(item.getNextBooking()).isEqualTo(booking);
        assertThat(item.getLastBooking()).isEqualTo(booking);
        assertThat(item.getBookings()).isEqualTo(List.of(booking));
        assertThat(item.getComments()).isEqualTo(List.of(comment));
        assertThat(item.getRequest()).isEqualTo(itemRequest);
    }

    @Test
    void allArgsConstructorTest() {
        Item item = new Item(this.item.getId(),
                this.item.getName(),
                this.item.getDescription(),
                this.item.getAvailable(),
                this.item.getOwner(),
                itemRequest,
                List.of(booking),
                List.of(comment),
                booking,
                booking);

        assertThat(item).isNotNull();
        assertThat(item.getId()).isEqualTo(this.item.getId());
        assertThat(item.getName()).isEqualTo(this.item.getName());
        assertThat(item.getDescription()).isEqualTo(this.item.getDescription());
        assertThat(item.getAvailable()).isEqualTo(this.item.getAvailable());
        assertThat(item.getNextBooking()).isEqualTo(booking);
        assertThat(item.getLastBooking()).isEqualTo(booking);
        assertThat(item.getBookings()).isEqualTo(List.of(booking));
        assertThat(item.getComments()).isEqualTo(List.of(comment));
        assertThat(item.getRequest()).isEqualTo(itemRequest);
    }

    @Test
    void allArgsConstructor_shouldThrowNotFoundExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> {
            new Item(this.item.getId(),
                    null,
                    this.item.getDescription(),
                    this.item.getAvailable(),
                    this.item.getOwner(),
                    itemRequest,
                    List.of(booking),
                    List.of(comment),
                    booking,
                    booking);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("name is marked non-null but is null");
    }

    @Test
    void allArgsConstructor_throwNotFoundExceptionWhenDescriptionIsNull() {
        assertThatThrownBy(() -> {
            new Item(this.item.getId(),
                    this.item.getName(),
                    null,
                    this.item.getAvailable(),
                    this.item.getOwner(),
                    itemRequest,
                    List.of(booking),
                    List.of(comment),
                    booking,
                    booking);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("description is marked non-null but is null");
    }

    @Test
    void allArgsConstructor_shouldThrowNotFoundExceptionWhenAvailableIsNull() {
        assertThatThrownBy(() -> {
            new Item(this.item.getId(),
                    this.item.getName(),
                    this.item.getDescription(),
                    null,
                    this.item.getOwner(),
                    itemRequest,
                    List.of(booking),
                    List.of(comment),
                    booking,
                    booking);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("available is marked non-null but is null");
    }

    @Test
    void allArgsConstructor_shouldThrowNotFoundExceptionWhenOwnerIsNull() {
        assertThatThrownBy(() -> {
            new Item(this.item.getId(),
                    this.item.getName(),
                    this.item.getDescription(),
                    this.item.getAvailable(),
                    null,
                    itemRequest,
                    List.of(booking),
                    List.of(comment),
                    booking,
                    booking);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("owner is marked non-null but is null");
    }

    @Test
    void builder_shouldReturnNotNull() {
        Item.ItemBuilder builder = Item.builder()
                .id(this.item.getId())
                .name(this.item.getName())
                .description(this.item.getDescription())
                .available(this.item.getAvailable())
                .owner(this.item.getOwner())
                .lastBooking(booking)
                .nextBooking(booking)
                .bookings(List.of(booking))
                .request(itemRequest)
                .comments(List.of(comment));

        assertThat(builder.toString()).contains(builder.getClass().getSimpleName());
        assertThat(builder.build()).isNotNull();
    }

    @Test
    void builder_shouldThrowNotFoundExceptionWhenNameIsNull() {
        assertThatThrownBy(() -> {
            Item.builder()
                    .id(this.item.getId())
                    .name(null)
                    .description(this.item.getDescription())
                    .available(this.item.getAvailable())
                    .owner(this.item.getOwner())
                    .lastBooking(booking)
                    .nextBooking(booking)
                    .bookings(List.of(booking))
                    .request(itemRequest)
                    .comments(List.of(comment))
                    .build();

        }).isInstanceOf(NullPointerException.class)
          .hasMessage("name is marked non-null but is null");
    }

    @Test
    void builder_throwNotFoundExceptionWhenDescriptionIsNull() {
        assertThatThrownBy(() -> {
            Item.builder()
                    .id(this.item.getId())
                    .name(this.item.getName())
                    .description(null)
                    .available(this.item.getAvailable())
                    .owner(this.item.getOwner())
                    .lastBooking(booking)
                    .nextBooking(booking)
                    .bookings(List.of(booking))
                    .request(itemRequest)
                    .comments(List.of(comment))
                    .build();

        }).isInstanceOf(NullPointerException.class)
                .hasMessage("description is marked non-null but is null");
    }

    @Test
    void builder_shouldThrowNotFoundExceptionWhenAvailableIsNull() {
        assertThatThrownBy(() -> {
            Item.builder()
                    .id(this.item.getId())
                    .name(this.item.getName())
                    .description(this.item.getDescription())
                    .available(null)
                    .owner(this.item.getOwner())
                    .lastBooking(booking)
                    .nextBooking(booking)
                    .bookings(List.of(booking))
                    .request(itemRequest)
                    .comments(List.of(comment))
                    .build();

        }).isInstanceOf(NullPointerException.class)
                .hasMessage("available is marked non-null but is null");
    }

    @Test
    void builder_shouldThrowNotFoundExceptionWhenOwnerIsNull() {
        assertThatThrownBy(() -> {
            Item.builder()
                    .id(this.item.getId())
                    .name(this.item.getName())
                    .description(this.item.getDescription())
                    .available(this.item.getAvailable())
                    .owner(null)
                    .lastBooking(booking)
                    .nextBooking(booking)
                    .bookings(List.of(booking))
                    .request(itemRequest)
                    .comments(List.of(comment))
                    .build();

        }).isInstanceOf(NullPointerException.class)
                .hasMessage("owner is marked non-null but is null");
    }

    @Test
    void builder_shouldClearBookings() {
        Item item = Item.builder()
                .id(this.item.getId())
                .name(this.item.getName())
                .description(this.item.getDescription())
                .available(this.item.getAvailable())
                .owner(this.item.getOwner())
                .lastBooking(booking)
                .nextBooking(booking)
                .booking(booking)
                .request(itemRequest)
                .comment(comment)
                .clearBookings()
                .build();

        assertThat(item.getBookings()).isEmpty();
    }

    @Test
    void builder_shouldClearComments() {
        Item item = Item.builder()
                .id(this.item.getId())
                .name(this.item.getName())
                .description(this.item.getDescription())
                .available(this.item.getAvailable())
                .owner(this.item.getOwner())
                .lastBooking(booking)
                .nextBooking(booking)
                .booking(booking)
                .request(itemRequest)
                .comment(comment)
                .clearComments()
                .build();

        assertThat(item.getComments()).isEmpty();
    }

    @Test
    void setName_shouldThrowNotFoundExceptionWhenNameIsNull() {
        Item item = new Item(this.item.getId(),
                this.item.getName(),
                this.item.getDescription(),
                this.item.getAvailable(),
                this.item.getOwner(),
                itemRequest,
                List.of(booking),
                List.of(comment),
                booking,
                booking);

        assertThatThrownBy(() -> {
            item.setName(null);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("name is marked non-null but is null");
    }

    @Test
    void setDescription_shouldThrowNotFoundExceptionWhenDescriptionIsNull() {
        Item item = new Item(this.item.getId(),
                this.item.getName(),
                this.item.getDescription(),
                this.item.getAvailable(),
                this.item.getOwner(),
                itemRequest,
                List.of(booking),
                List.of(comment),
                booking,
                booking);

        assertThatThrownBy(() -> {
            item.setDescription(null);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("description is marked non-null but is null");
    }

    @Test
    void setAvailable_shouldThrowNotFoundExceptionWhenAvailableIsNull() {
        Item item = new Item(this.item.getId(),
                this.item.getName(),
                this.item.getDescription(),
                this.item.getAvailable(),
                this.item.getOwner(),
                itemRequest,
                List.of(booking),
                List.of(comment),
                booking,
                booking);

        assertThatThrownBy(() -> {
            item.setAvailable(null);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("available is marked non-null but is null");
    }

    @Test
    void setOwner_shouldThrowNotFoundExceptionWhenOwnerIsNull() {
        Item item = new Item(this.item.getId(),
                this.item.getName(),
                this.item.getDescription(),
                this.item.getAvailable(),
                this.item.getOwner(),
                itemRequest,
                List.of(booking),
                List.of(comment),
                booking,
                booking);

        assertThatThrownBy(() -> {
            item.setOwner(null);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("owner is marked non-null but is null");
    }

    @Test
    void toStringTest() {
        assertThat(item.toString()).startsWith(item.getClass().getSimpleName());
    }
}