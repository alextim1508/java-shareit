package ru.practicum.shareit.integration;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.ShortBookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.FullItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDtoIn;
import ru.practicum.shareit.user.dto.UserDtoOut;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exception.ForbiddenException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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

    UserDtoIn owner, user;
    ItemDtoIn itemDtoIn;

    @BeforeEach
    void setUp() {
        owner = UserDtoIn.builder()
                .name("owner")
                .email("owner@gmail.com")
                .build();

        user = UserDtoIn.builder()
                .name("user")
                .email("user@gmail.com")
                .build();

        itemDtoIn = ItemDtoIn.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    @Test
    public void shouldSaveItem() {
        UserDtoOut savedOwner = userService.create(owner);

        ItemDtoOut savedItem = (ItemDtoOut) itemService.create(itemDtoIn, savedOwner.getId());

        final int OTHER_ID = 10;
        assertThat(OTHER_ID).isNotEqualTo(savedOwner.getId());

        FullItemDtoOut byId = (FullItemDtoOut) itemService.getById(savedItem.getId(), OTHER_ID);

        assertThat(byId.getId())
                .isEqualTo(savedItem.getId());
    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void shouldGetItemWithLastAndNextBooking() {
        UserDtoOut savedOwner = userService.create(owner);

        UserDtoOut savedUser = userService.create(user);

        ItemDtoOut savedItem = (ItemDtoOut) itemService.create(itemDtoIn, savedOwner.getId());

        LocalDateTime now = LocalDateTime.now().withNano(0);

        BookingDtoOut booking1 = (BookingDtoOut) bookingService.create(BookingDtoIn.builder()
                        .startDate(now.minusDays(5))
                        .endDate(now.minusDays(4))
                        .itemId(savedItem.getId())
                        .build(),
                savedUser.getId());

        BookingDtoOut booking2 = (BookingDtoOut) bookingService.create(BookingDtoIn.builder()
                        .startDate(now.minusDays(3))
                        .endDate(now.minusDays(2))
                        .itemId(savedItem.getId())
                        .build(),
                savedUser.getId());

        BookingDtoOut booking3 = (BookingDtoOut) bookingService.create(BookingDtoIn.builder()
                        .startDate(now.plusDays(1))
                        .endDate(now.plusDays(2))
                        .itemId(savedItem.getId())
                        .build(),
                savedUser.getId());

        BookingDtoOut booking4 = (BookingDtoOut) bookingService.create(BookingDtoIn.builder()
                        .startDate(now.plusDays(3))
                        .endDate(now.plusDays(4))
                        .itemId(savedItem.getId())
                        .build(),
                savedUser.getId());


        FullItemDtoOut byId = (FullItemDtoOut) itemService.getById(savedItem.getId(), savedOwner.getId());

        ShortBookingDtoOut expectedBooking2 = ShortBookingDtoOut.builder()
                .id(booking2.getId())
                .bookerId(booking2.getBooker().getId())
                .startDate(booking2.getStartDate())
                .endDate(booking2.getEndDate())
                .build();

        ShortBookingDtoOut expectedBooking3 = ShortBookingDtoOut.builder()
                .id(booking3.getId())
                .bookerId(booking3.getBooker().getId())
                .startDate(booking3.getStartDate())
                .endDate(booking3.getEndDate())
                .build();

        assertThat(byId.getLastBooking()).isEqualTo(expectedBooking2);
        assertThat(byId.getNextBooking()).isEqualTo(expectedBooking3);
    }

    @Test
    public void shouldSaveItemWithRequest() {
        UserDtoOut savedUser = userService.create(owner);
        UserDtoOut savedOwner = userService.create(user);

        ItemRequestDtoIn description = new ItemRequestDtoIn("description");

        ItemRequestDtoOut request = (ItemRequestDtoOut) itemRequestService.create(description, savedUser.getId());

        itemDtoIn.setRequestId(request.getId());

        ItemDtoOut savedItem = (ItemDtoOut) itemService.create(itemDtoIn, savedOwner.getId());

        final int OTHER_ID = 10;
        assertThat(OTHER_ID).isNotEqualTo(savedItem.getId()).isNotEqualTo(savedUser.getId());

        FullItemDtoOut byId = (FullItemDtoOut) itemService.getById(savedItem.getId(), OTHER_ID);

        assertThat(byId.getId()).isEqualTo(savedItem.getId());
    }

    @Test
    public void shouldThrowExceptionWhenItemIsMissing() {
        assertThatThrownBy(() -> {
            itemService.getById(1, 1);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void shouldGetAvailableItemByOwner() {
        UserDtoOut savedOwner = userService.create(owner);

        ItemDtoOut savedItem = (ItemDtoOut) itemService.create(itemDtoIn, savedOwner.getId());


        UserDtoIn owner2 = UserDtoIn.builder()
                .name("user2")
                .email("user2@gmail.com")
                .build();

        ItemDtoIn itemDtoIn2 = ItemDtoIn.builder()
                .name("name2")
                .description("description2")
                .available(true)
                .build();


        UserDtoOut savedOwner2 = userService.create(owner2);

        ItemDtoOut savedItem2 = (ItemDtoOut) itemService.create(itemDtoIn2, savedOwner2.getId());

        FullItemDtoOut expected = FullItemDtoOut.builder()
                .id(savedItem.getId())
                .name(savedItem.getName())
                .description(savedItem.getDescription())
                .available(savedItem.getAvailable())
                .comments(Collections.emptyList())
                .build();


        assertThat(itemService.getAvailableItemByOwner(savedOwner.getId()).get(0))
                .isEqualTo(expected);
    }

    @Test
    public void shouldGetAvailableItemByPattern() {
        UserDtoOut savedOwner = userService.create(owner);

        ItemDtoOut savedItem1 = (ItemDtoOut) itemService.create(ItemDtoIn.builder()
                .name("name")
                .description("AsDfg")
                .available(true)
                .build(), savedOwner.getId());

        ItemDtoOut savedItem2 = (ItemDtoOut) itemService.create(ItemDtoIn.builder()
                .name("name")
                .description("asd")
                .available(true)
                .build(), savedOwner.getId());

        ItemDtoOut savedItem3 = (ItemDtoOut) itemService.create(ItemDtoIn.builder()
                .name("name")
                .description("aSD")
                .available(true)
                .build(), savedOwner.getId());

        ItemDtoOut savedItem4 = (ItemDtoOut) itemService.create(ItemDtoIn.builder()
                .name("name")
                .description("description")
                .available(true)
                .build(), savedOwner.getId());

        assertThat(itemService.getAvailableItemByPattern("asd"))
                .hasSize(3);
    }

    @Test
    public void shouldThrowExceptionWhenUserDoesNotOwner() {
        UserDtoOut savedOwner = userService.create(owner);
        UserDtoOut savedUser = userService.create(user);

        ItemDtoOut savedItem = (ItemDtoOut) itemService.create(itemDtoIn, savedOwner.getId());

        assertThatThrownBy(() -> {
            itemService.update(savedItem.getId(), null, savedUser.getId());
        }).isInstanceOf(ForbiddenException.class);
    }

    @Test
    public void shouldUpdateItem() {
        UserDtoOut savedOwner = userService.create(owner);
        UserDtoOut savedUser = userService.create(user);
        ItemDtoOut savedItem = (ItemDtoOut) itemService.create(itemDtoIn, savedOwner.getId());

        itemService.update(savedItem.getId(), ItemDtoIn.builder().name("newName").build(), savedOwner.getId());
        assertThat(itemService.getById(savedItem.getId(), savedUser.getId())).extracting(item -> ((FullItemDtoOut) item).getName())
                .isEqualTo("newName");

        itemService.update(savedItem.getId(), ItemDtoIn.builder().description("newDescription").build(), savedOwner.getId());
        assertThat(itemService.getById(savedItem.getId(), savedUser.getId())).extracting(item -> ((FullItemDtoOut) item).getDescription())
                .isEqualTo("newDescription");

        itemService.update(savedItem.getId(), ItemDtoIn.builder().available(true).build(), savedOwner.getId());
        assertThat(itemService.getById(savedItem.getId(), savedUser.getId())).extracting(item -> ((FullItemDtoOut) item).getAvailable())
                .isEqualTo(true);

        itemService.update(savedItem.getId(), ItemDtoIn.builder().available(false).build(), savedOwner.getId());
        assertThat(itemService.getById(savedItem.getId(), savedUser.getId())).extracting(item -> ((FullItemDtoOut) item).getAvailable())
                .isEqualTo(false);
    }

    @Test
    public void shouldDeleteItem() {
        UserDtoOut savedOwner = userService.create(owner);
        ItemDtoOut savedItem1 = (ItemDtoOut) itemService.create(itemDtoIn, savedOwner.getId());

        ItemDtoOut savedItem2 = (ItemDtoOut) itemService.create(
                ItemDtoIn.builder()
                        .name("name2")
                        .description("description2")
                        .available(true)
                        .build(), savedOwner.getId());

        itemService.delete(savedItem1.getId());

        assertThat(itemService.getAvailableItemByOwner(savedOwner.getId()))
                .hasSize(1);
    }

}
