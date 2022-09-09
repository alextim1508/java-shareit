package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.controller.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exception.ActionIsNotAvailableException;
import ru.practicum.shareit.util.exception.ForbiddenException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.model.BookingStatus.*;

@SpringBootTest
public class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @MockBean
    ItemRepository itemRepo;
    @MockBean
    CommentRepository commentRepo;
    @MockBean
    UserService userService;
    @MockBean
    ItemMapper itemMapper;
    User owner;
    Item item;
    ItemDto itemDto;

    @BeforeEach
    void setUp() {
        doNothing().when(userService).existenceCheck(anyInt());

        owner = User.builder()
                .id(1)
                .name("owner")
                .email("owner@gmail.com")
                .build();

        item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        itemDto = ItemDto.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .build();
    }

    @Test
    void create_shouldInvokeRepositoryAndReturnTheSame() {
        when(itemRepo.save(item))
                .thenReturn(item);

        Item savedItem = itemService.create(item);

        assertThat(savedItem)
                .isEqualTo(item);

        verify(itemRepo, times(1)).save(item);
    }

    @Test
    void getById_shouldThrowNotFoundExceptionWhenRepositoryReturnEmpty() {
        when(itemRepo.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getById(1, 1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Item with ID 1 is not found");

        verify(itemRepo, times(1)).findById(1);
    }

    @Test
    void getById_shouldInvokeRepositoryAndReturnTheSame() {
        when(itemRepo.findById(anyInt()))
                .thenReturn(Optional.of(item));

        Item itemById = itemService.getById(1, 1);

        assertThat(itemById).isEqualTo(item);
        verify(itemRepo, times(1)).findById(1);
    }

    @Test
    void getAvailableItemByOwner_shouldInvokeRepositoryAndReturnTheSame() {
        when(itemRepo.findByOwner(anyInt()))
                .thenReturn(Collections.singletonList(item));

        List<Item> items = itemService.getAvailableItemByOwner(1);

        assertThat(items)
                .containsAll(Collections.singletonList(item));

        verify(itemRepo, times(1)).findByOwner(1);
    }

    @Test
    void getAvailableItemByPattern_shouldInvokeRepositoryAndReturnTheSame() {
        when(itemRepo.findAvailableItemsByNameOrDescription(anyString()))
                .thenReturn(Collections.singletonList(item));

        List<Item> items = itemService.getAvailableItemByPattern("desc");

        assertThat(items)
                .containsAll(Collections.singletonList(item));

        verify(itemRepo, times(1)).findAvailableItemsByNameOrDescription("desc");
    }

    @Test
    void getAvailableItemByPattern_shouldGetEmptyItemWhenPatternIsNull() {
        List<Item> items = itemService.getAvailableItemByPattern(null);

        assertThat(items)
                .isEmpty();

        verify(itemRepo, never()).findAvailableItemsByNameOrDescription(null);
    }

    @Test
    void getAvailableItemByPattern_shouldGetEmptyItemWhenPatternIsEmpty() {
        List<Item> items = itemService.getAvailableItemByPattern("");

        assertThat(items)
                .isEmpty();

        verify(itemRepo, never()).findAvailableItemsByNameOrDescription("");
    }

    @Test
    void getAvailableItemByPattern_shouldGetEmptyItemWhenPatternIsBlank() {
        List<Item> items = itemService.getAvailableItemByPattern(" ");

        assertThat(items)
                .isEmpty();

        verify(itemRepo, never()).findAvailableItemsByNameOrDescription(" ");
    }

    @Test
    void update_shouldThrowExceptionWhenUserIsNotOwner() {
        when(itemRepo.findById(anyInt()))
                .thenReturn(Optional.of(item));

        assertThatThrownBy(() -> itemService.update(1, any(ItemDto.class), 2))
                .isInstanceOf(ForbiddenException.class);

        verify(itemRepo, times(1)).findById(1);
    }

    @Test
    void update_shouldGetFromRepositoryAndPatchAndSaveAndReturnSaved() {
        when(itemRepo.findById(anyInt()))
                .thenReturn(Optional.of(item));
        doNothing().when(itemMapper)
                .updateItemFromDto(any(ItemDto.class), any(Item.class));

        Item updatedItem = itemService.update(1, itemDto, 1);

        assertThat(updatedItem).isEqualTo(item);

        verify(itemRepo, times(1)).findById(1);
    }

    @Test
    void delete_shouldInvokeRepositoryDelete() {
        doNothing().when(itemRepo)
                .deleteById(anyInt());

        itemService.delete(1);
        verify(itemRepo, times(1)).deleteById(1);
    }

    @Test
    void delete_shouldThrowNotFoundExceptionWhenInvokeRepositoryWithWrongId() {
        doThrow(EmptyResultDataAccessException.class).when(itemRepo)
                .deleteById(anyInt());

        assertThatThrownBy(() -> itemService.delete(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Item with ID 1 is not found");
    }

    @Test
    void createComment_shouldThrowExceptionWhenBookingIsCanceled() {
        LocalDateTime now = LocalDateTime.now();

        User booker = new User(2, "user", "user@gmail.com");
        Booking booking = Booking.builder()
                .startDate(now.minusDays(2))
                .endDate(now.minusDays(1))
                .item(item)
                .booker(booker)
                .status(CANCELED)
                .build();

        item.setBookings(List.of(booking));

        Comment comment = new Comment("comment", item, booker);

        assertThat(booker.getId())
                .isNotEqualTo(owner.getId());

        assertThatThrownBy(() -> {
            itemService.create(comment);
        }).isInstanceOf(ActionIsNotAvailableException.class);
    }

    @Test
    void createComment_shouldThrowExceptionWhenBookingIsWaiting() {
        LocalDateTime now = LocalDateTime.now();

        final int ITEM_ID = 1;
        final int BOOKER_ID = 1;

        final int OWNER_ID = 2;
        Item item = Item.builder()
                .id(ITEM_ID)
                .name("item")
                .description("description")
                .available(true)
                .owner(new User(OWNER_ID, "user", "user@gmail.com"))
                .build();

        User booker = new User(BOOKER_ID, "user", "user@gmail.com");
        Booking booking = Booking.builder()
                .startDate(now.minusDays(2))
                .endDate(now.minusDays(1))
                .item(item)
                .booker(booker)
                .status(WAITING)
                .build();

        item.setBookings(List.of(booking));

        Comment comment = new Comment("comment", item, booker);

        assertThat(BOOKER_ID).isNotEqualTo(OWNER_ID);

        assertThatThrownBy(() -> {
            itemService.create(comment);
        }).isInstanceOf(ActionIsNotAvailableException.class);
    }

    @Test
    void createComment_shouldThrowExceptionWhenBookingIsRejected() {
        LocalDateTime now = LocalDateTime.now();

        final int ITEM_ID = 1;
        final int BOOKER_ID = 1;

        final int OWNER_ID = 2;
        Item item = Item.builder()
                .id(ITEM_ID)
                .name("item")
                .description("description")
                .available(true)
                .owner(new User(OWNER_ID, "user", "user@gmail.com"))
                .build();

        User booker = new User(BOOKER_ID, "user", "user@gmail.com");
        Booking booking = Booking.builder()
                .startDate(now.minusDays(2))
                .endDate(now.minusDays(1))
                .item(item)
                .booker(booker)
                .status(REJECTED)
                .build();

        item.setBookings(List.of(booking));

        Comment comment = new Comment("comment", item, booker);

        assertThat(BOOKER_ID).isNotEqualTo(OWNER_ID);

        assertThatThrownBy(() -> {
            itemService.create(comment);
        }).isInstanceOf(ActionIsNotAvailableException.class);
    }

    @Test
    void createComment_shouldThrowExceptionWhenUserWasNotBooker() {
        LocalDateTime now = LocalDateTime.now();

        final int ITEM_ID = 1;
        final int BOOKER_ID = 1;

        final int OWNER_ID = 2;
        Item item = Item.builder()
                .id(ITEM_ID)
                .name("item")
                .description("description")
                .available(true)
                .owner(new User(OWNER_ID, "user", "user1@gmail.com"))
                .build();

        User booker = new User(BOOKER_ID, "user", "user2@gmail.com");
        Booking booking = Booking.builder()
                .startDate(now.minusDays(2))
                .endDate(now.minusDays(1))
                .item(item)
                .booker(booker)
                .status(CANCELED)
                .build();

        item.setBookings(List.of(booking));

        final int USER_ID = 3;
        User user = new User(USER_ID, "user", "user3@gmail.com");

        Comment comment = new Comment("comment", item, user);

        assertThat(USER_ID).isNotEqualTo(BOOKER_ID);

        assertThatThrownBy(() -> {
            itemService.create(comment);
        }).isInstanceOf(ActionIsNotAvailableException.class);
    }

    @Test
    void createComment_shouldThrowExceptionWhenBookingIsNotFinished() {
        LocalDateTime now = LocalDateTime.now();

        final int ITEM_ID = 1;
        final int BOOKER_ID = 1;

        final int OWNER_ID = 2;
        Item item = Item.builder()
                .id(ITEM_ID)
                .name("item")
                .description("description")
                .available(true)
                .owner(new User(OWNER_ID, "user", "user@gmail.com"))
                .build();

        User booker = new User(BOOKER_ID, "user", "user@gmail.com");
        Booking booking = Booking.builder()
                .startDate(now.minusDays(2))
                .endDate(now.plusDays(1))
                .item(item)
                .booker(booker)
                .status(REJECTED)
                .build();

        item.setBookings(List.of(booking));

        Comment comment = new Comment("comment", item, booker);

        assertThat(BOOKER_ID).isNotEqualTo(OWNER_ID);

        assertThat(booking.getEndDate()).isAfter(now);

        assertThatThrownBy(() -> {
            itemService.create(comment);
        }).isInstanceOf(ActionIsNotAvailableException.class);
    }

    @Test
    void createComment_shouldInvokeRepositoryAndReturnTheSame() {
        LocalDateTime now = LocalDateTime.now();

        final int ITEM_ID = 1;
        final int BOOKER_ID = 1;

        final int OWNER_ID = 2;
        Item item = Item.builder()
                .id(ITEM_ID)
                .name("item")
                .description("description")
                .available(true)
                .owner(new User(OWNER_ID, "user", "user@gmail.com"))
                .build();

        User booker = new User(BOOKER_ID, "user", "user@gmail.com");
        Booking booking = Booking.builder()
                .startDate(now.minusDays(2))
                .endDate(now.minusDays(1))
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();

        item.setBookings(List.of(booking));

        Comment comment = new Comment("comment", item, booker);

        final int COMMENT_ID = 1;
        when(commentRepo.save(comment))
                .thenAnswer(invocationOnMock -> {
                    Comment argument = invocationOnMock.getArgument(0);
                    argument.setId(COMMENT_ID);
                    return argument;
                });

        Comment savedComment = itemService.create(comment);

        verify(commentRepo, times(1)).save(comment);
        assertThat(savedComment).extracting(Comment::getId, Comment::getText, Comment::getItem, Comment::getAuthor)
                .containsExactly(COMMENT_ID, comment.getText(), comment.getItem(), comment.getAuthor());
    }
}
