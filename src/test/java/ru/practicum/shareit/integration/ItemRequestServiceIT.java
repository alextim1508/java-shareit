package ru.practicum.shareit.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
public class ItemRequestServiceIT {

    @Autowired
    ItemRequestService itemRequestService;

    @Autowired
    UserService userService;

    @Test
    void shouldSaveItemRequest() {
        User requestor = new User("user", "mail@gmail.com");
        userService.create(requestor);

        ItemRequest itemRequest = new ItemRequest("description", requestor);
        itemRequestService.create(itemRequest);
    }

    @Test
    public void shouldThrowExceptionWhenItemRequestIsMissing() {
        assertThatThrownBy(() -> {
            itemRequestService.getById(1, 1);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldGetById() {
        User requestor = new User("user", "mail@gmail.com");
        userService.create(requestor);

        ItemRequest itemRequest = new ItemRequest("description", requestor);
        itemRequestService.create(itemRequest);

        assertThat(itemRequestService.getById(itemRequest.getId(), requestor.getId())).isEqualTo(itemRequest);
    }

    @Test
    void shouldGetAllByRequestor() {
        User requestor1 = new User("user", "mail1@gmail.com");
        userService.create(requestor1);

        User requestor2 = new User("user", "mail2@gmail.com");
        userService.create(requestor2);

        ItemRequest itemRequest1 = new ItemRequest("description", requestor1);
        itemRequestService.create(itemRequest1);
        ItemRequest itemRequest2 = new ItemRequest("description", requestor1);
        itemRequestService.create(itemRequest2);
        ItemRequest itemRequest3 = new ItemRequest("description", requestor2);
        itemRequestService.create(itemRequest3);

        assertThat(itemRequestService.getAllByRequestor(requestor1.getId())).hasSize(2)
                .hasSameElementsAs(Arrays.asList(itemRequest1, itemRequest2));
    }

    @Test
    void shouldGetAll() {
        User requestor1 = new User("user", "mail1@gmail.com");
        userService.create(requestor1);

        User requestor2 = new User("user", "mail2@gmail.com");
        userService.create(requestor2);

        ItemRequest itemRequest1 = new ItemRequest("description", requestor1);
        itemRequestService.create(itemRequest1);
        ItemRequest itemRequest2 = new ItemRequest("description", requestor1);
        itemRequestService.create(itemRequest2);
        ItemRequest itemRequest3 = new ItemRequest("description", requestor1);
        itemRequestService.create(itemRequest3);

        assertThat(itemRequestService.getAll(requestor2.getId(), 1, 10)).hasSize(2)
                .hasSameElementsAs(Arrays.asList(itemRequest2, itemRequest1));
    }
}
