package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemBaseTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    void toStringTest() {
        assertThat(item.toString()).startsWith(item.getClass().getSimpleName());
    }
}