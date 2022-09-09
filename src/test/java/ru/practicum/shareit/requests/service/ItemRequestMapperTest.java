package ru.practicum.shareit.requests.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.item.controller.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.controller.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemRequestMapperTest {

    @Autowired
    ItemRequestMapper mapper;

    @MockBean
    UserService userService;

    User owner, requester;
    Item item;
    ItemRequest itemRequest;
    ItemDto itemDto;
    ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        doNothing().when(userService).existenceCheck(anyInt());

        LocalDateTime now = LocalDateTime.now();

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
        itemRequest = ItemRequest.builder()
                .id(1)
                .description("description")
                .requestor(requester)
                .created(now)
                .items(List.of(item))
                .build();

        itemDto = ItemDto.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .comments(Collections.emptyList())
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("description")
                .created(now)
                .items(Arrays.asList(itemDto))
                .build();
    }


    @Test
    void toDto_shouldInvokeServiceAndReturnTheSame() {
        assertThat(mapper.toDto(itemRequest)).isEqualTo(itemRequestDto);
    }

    @Test
    void toDtoList_shouldInvokeServiceAndReturnTheSame() {
        assertThat(mapper.toDto(Arrays.asList(itemRequest))).isEqualTo(Arrays.asList(itemRequestDto));
    }

    @Test
    void fromDto_shouldInvokeServiceAndReturnTheSame() {
        when(userService.getById(anyInt())).thenReturn(requester);

        ItemRequestDto itemRequestDto = new ItemRequestDto("description");

        int requesterId = 1;

        ItemRequest expected = ItemRequest.builder()
                .description("description")
                .requestor(requester)
                .build();

        assertThat(mapper.fromDto(itemRequestDto, requesterId)).isEqualTo(expected);
    }
}
