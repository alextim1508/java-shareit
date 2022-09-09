package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.item.controller.CommentDto;
import ru.practicum.shareit.item.controller.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestFactory;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemMapperTest {

    @Autowired
    ItemMapper mapper;

    @MockBean
    UserService userService;

    @MockBean
    ItemRequestFactory itemRequestFactory;

    User requestor, owner, commentator;
    ItemRequest itemRequest;
    Item item;
    ItemDto itemDto;
    Comment comment;
    CommentDto commentDto;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        requestor = User.builder()
                .id(1)
                .name("requestor")
                .email("requestor@gmail.com")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1)
                .description("description")
                .requestor(requestor)
                .build();

        owner = User.builder()
                .id(2)
                .name("owner")
                .email("owner@gmail.com")
                .build();
        item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .request(itemRequest)
                .comments(Collections.emptyList())
                .build();

        commentator = User.builder()
                .id(3)
                .name("commentator")
                .email("commentator@gmail.com")
                .build();

        comment = new Comment(1, "comment", item, commentator, now);
        item.setComments(List.of(comment));


        commentDto = new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getCreated());
        itemDto = ItemDto.builder()
                .id(item.getId())
                .name("item")
                .description("description")
                .available(true)
                .requestId(itemRequest.getId())
                .comments(Collections.singletonList(commentDto))
                .build();
    }

    @Test
    void toDto_shouldInvokeServiceAndReturnTheSame() {
        assertThat(mapper.toDto(item)).isEqualTo(itemDto);
    }

    @Test
    void toDto_shouldInvokeServiceAndReturnTheSameWithoutItemRequestId() {
        item.getRequest().setId(null);
        itemDto.setRequestId(null);
        assertThat(mapper.toDto(item)).isEqualTo(itemDto);
    }


    @Test
    void toDto_shouldInvokeServiceAndReturnNull() {
        Item item = null;
        assertThat(mapper.toDto(item)).isNull();
    }

    @Test
    void toDtoList_shouldInvokeServiceAndReturnTheSame() {
        assertThat(mapper.toDto(List.of(item))).isEqualTo(List.of(itemDto));
    }

    @Test
    void toDtoList_shouldInvokeServiceAndReturnNull() {
        List<Item> items = null;
        assertThat(mapper.toDto(items)).isNull();
    }

    @Test
    void fromDto_shouldInvokeServiceAndReturnTheSame() {
        when(userService.getById(anyInt())).thenReturn(owner);
        when(itemRequestFactory.getById(anyInt())).thenReturn(itemRequest);

        assertThat(mapper.fromDto(itemDto, owner.getId())).isEqualTo(item);
    }

    @Test
    void fromDto_shouldInvokeServiceAndReturnNull() {
        assertThat(mapper.fromDto(null, null)).isNull();
    }

    @Test
    void updateItemFromDto_shouldInvokeServiceAndReturnTheSame() {
        ItemDto itemDto = ItemDto.builder()
                .name("newItem")
                .description("newDescription")
                .available(false)
                .build();

        Item expected = Item.builder()
                .id(item.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();

        mapper.updateItemFromDto(itemDto, item);

        assertThat(item).isEqualTo(expected);
    }

    @Test
    void updateItemFromDto_shouldInvokeServiceAndReturnUpdatedName() {
        ItemDto itemDto = ItemDto.builder()
                .name("newItem")
                .build();

        Item expected = Item.builder()
                .id(item.getId())
                .name(itemDto.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();

        mapper.updateItemFromDto(itemDto, item);

        assertThat(item).isEqualTo(expected);
    }

    @Test
    void updateItemFromDto_shouldInvokeServiceAndReturnUpdatedDescription() {
        ItemDto itemDto = ItemDto.builder()
                .description("newDescription")
                .build();

        Item expected = Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(itemDto.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();

        mapper.updateItemFromDto(itemDto, item);

        assertThat(item).isEqualTo(expected);
    }

    @Test
    void updateItemFromDto_shouldInvokeServiceAndReturnUpdatedAvailable() {
        ItemDto itemDto = ItemDto.builder()
                .available(false)
                .build();

        Item expected = Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(itemDto.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();

        mapper.updateItemFromDto(itemDto, item);

        assertThat(item).isEqualTo(expected);
    }
}
