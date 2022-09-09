package ru.practicum.shareit.integration;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.controller.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exception.ForbiddenException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;

@IntegrationTest
public class ItemServiceIT {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    BookingService bookingService;

    @Autowired
    ItemRequestService itemRequestService;

    @Test
    public void shouldSaveItem() {
        User owner = userService.create(new User("user", "mail@gmail.com"));
        Item savedItem = itemService.create(new Item("name", "description", true, owner));
        final int OTHER_ID = 10;
        assertThat(OTHER_ID).isNotEqualTo(owner.getId());
        Item byId = itemService.getById(savedItem.getId(), OTHER_ID);
        assertThat(byId).isEqualTo(savedItem);
    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void shouldGetItemWithLastAndNextBooking() {
        User owner = userService.create(new User("user", "owner@gmail.com"));
        User user = userService.create(new User("user", "user@gmail.com"));

        Item item = itemService.create(new Item("name", "description", true, owner));

        LocalDateTime now = LocalDateTime.now().withNano(0);

        Booking booking1 = bookingService.create(new Booking(
                now.minusDays(5),
                now.minusDays(4),
                item,
                user));

        Booking booking2 = bookingService.create(new Booking(
                now.minusDays(3),
                now.minusDays(2),
                item,
                user));

        Booking booking3 = bookingService.create(new Booking(
                now.plusDays(1),
                now.plusDays(2),
                item,
                user));

        Booking booking4 = bookingService.create(new Booking(
                now.plusDays(3),
                now.plusDays(4),
                item,
                user));

        Item byId = itemService.getById(item.getId(), owner.getId());

        assertThat(byId.getLastBooking()).isEqualTo(booking2);
        assertThat(byId.getNextBooking()).isEqualTo(booking3);
    }

    @Test
    public void shouldSaveItemWithRequest() {
        User user = userService.create(new User("user", "user@gmail.com"));
        User owner = userService.create(new User("user", "owner@gmail.com"));

        ItemRequest request = itemRequestService.create(new ItemRequest("description", user));

        Item item = new Item("name", "description", true, owner);
        item.setRequest(request);
        Item savedItem = itemService.create(item);

        final int OTHER_ID = 10;
        assertThat(OTHER_ID).isNotEqualTo(owner.getId()).isNotEqualTo(user.getId());
        assertThat(itemService.getById(savedItem.getId(), OTHER_ID)).isEqualTo(savedItem);
    }

    @Test
    public void shouldThrowExceptionWhenItemIsMissing() {
        assertThatThrownBy(() -> {
            itemService.getById(1, 1);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void shouldGetAvailableItemByOwner() {
        User owner1 = userService.create(new User("user", "mail1@gmail.com"));
        Item savedItem1 = itemService.create(new Item("name", "description", true, owner1));

        User owner2 = userService.create(new User("user", "mail2@gmail.com"));
        Item savedItem2 = itemService.create(new Item("name", "description", true, owner2));

        assertThat(itemService.getAvailableItemByOwner(owner1.getId()))
                .hasSize(1)
                .containsAll(Collections.singleton(savedItem1));
    }

    @Test
    public void shouldGetAvailableItemByPattern() {
        User owner1 = userService.create(new User("user", "mail1@gmail.com"));
        Item savedItem1 = itemService.create(new Item("name", "AsDfg", true, owner1));
        Item savedItem2 = itemService.create(new Item("name", "asd", true, owner1));
        Item savedItem3 = itemService.create(new Item("name", "aSD", true, owner1));
        Item savedItem4 = itemService.create(new Item("name", "description", true, owner1));

        assertThat(itemService.getAvailableItemByPattern("asd"))
                .hasSize(3)
                .containsAll(Arrays.asList(savedItem1, savedItem2, savedItem3));

        savedItem1 = itemService.create(new Item("FsS", "description", true, owner1));
        savedItem2 = itemService.create(new Item("Fss", "description", true, owner1));
        savedItem3 = itemService.create(new Item("Fss", "desfsstion", true, owner1));
        savedItem4 = itemService.create(new Item("name", "description", true, owner1));

        assertThat(itemService.getAvailableItemByPattern("fss"))
                .hasSize(3)
                .containsAll(Arrays.asList(savedItem1, savedItem2, savedItem3));
    }

    @Test
    public void shouldThrowExceptionWhenUserDoesNotOwner() {
        User owner = userService.create(new User("user", "mail1@gmail.com"));
        User user = userService.create(new User("user", "mail2@gmail.com"));
        Item savedItem = itemService.create(new Item("name", "description", true, owner));

        assertThatThrownBy(() -> {
            itemService.update(savedItem.getId(), null, user.getId());
        }).isInstanceOf(ForbiddenException.class);
    }

    @Test
    public void shouldUpdateItem() {
        User owner1 = userService.create(new User("user", "mail1@gmail.com"));

        Item savedItem = itemService.create(new Item("name", "description", true, owner1));

        itemService.update(savedItem.getId(), ItemDto.builder().name("newName").build(), owner1.getId());
        assertThat(itemService.getById(savedItem.getId(), owner1.getId())).extracting(Item::getName)
                .isEqualTo("newName");

        itemService.update(savedItem.getId(), ItemDto.builder().description("newDescription").build(), owner1.getId());
        assertThat(itemService.getById(savedItem.getId(), owner1.getId())).extracting(Item::getDescription)
                .isEqualTo("newDescription");

        itemService.update(savedItem.getId(), ItemDto.builder().available(true).build(), owner1.getId());
        assertThat(itemService.getById(savedItem.getId(), owner1.getId())).extracting(Item::getAvailable)
                .isEqualTo(true);

        itemService.update(savedItem.getId(), ItemDto.builder().available(false).build(), owner1.getId());
        assertThat(itemService.getById(savedItem.getId(), owner1.getId())).extracting(Item::getAvailable)
                .isEqualTo(false);
    }

    @Test
    public void shouldDeleteItem() {
        User owner = userService.create(new User("user", "mail1@gmail.com"));
        Item savedItem1 = itemService.create(new Item("name1", "description", true, owner));
        Item savedItem2 = itemService.create(new Item("name2", "description", true, owner));
        itemService.delete(savedItem1.getId());

        assertThat(itemService.getAvailableItemByOwner(owner.getId()))
                .hasSize(1)
                .containsAll(Collections.singleton(savedItem2));
    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void shouldSaveComment() {
        User owner = userService.create(new User("user", "mail@gmail.com"));
        Item savedItem = itemService.create(new Item("name", "description", true, owner));

        User booker = userService.create(new User("booker", "booker@gmail.com"));
        LocalDateTime now = LocalDateTime.now().withNano(0);

        Booking booking = bookingService.create(new Booking(
                now.minusDays(2),
                now.minusDays(1),
                savedItem,
                booker));
        booking.setStatus(APPROVED);

        savedItem.setBookings(List.of(booking));

        Comment savedComment = itemService.create(new Comment("text", savedItem, booker));


        Item byId = itemService.getById(savedItem.getId(), owner.getId());
        assertThat(byId.getComments().get(0)).isEqualTo(savedComment);

        assertThat(byId.getComments().get(0).getCreated()).isEqualTo(savedComment.getCreated());
    }
}
