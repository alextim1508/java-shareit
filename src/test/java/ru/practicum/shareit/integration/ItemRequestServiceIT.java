package ru.practicum.shareit.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDtoIn;
import ru.practicum.shareit.user.dto.UserDtoOut;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exception.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
public class ItemRequestServiceIT {

    @Autowired
    ItemRequestService itemRequestService;

    @Autowired
    UserService userService;


    UserDtoOut savedRequestor;

    @BeforeEach
    void setUp() {
        UserDtoIn requestor = UserDtoIn.builder()
                .name("requestor")
                .email("requestor@gmail.com")
                .build();

        savedRequestor = userService.create(requestor);
    }


    @Test
    public void shouldThrowExceptionWhenItemRequestIsMissing() {
        assertThatThrownBy(() -> {
            itemRequestService.getById(1, 1);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldGetById() {
        ItemRequestDtoIn itemRequestIn = new ItemRequestDtoIn("description");
        ItemRequestDtoOut savedItemRequest = (ItemRequestDtoOut) itemRequestService.create(itemRequestIn, savedRequestor.getId());

        assertThat(itemRequestService.getById(savedItemRequest.getId(), savedItemRequest.getId())).isEqualTo(savedItemRequest);
    }

    @Test
    void shouldGetAllByRequestor() {
        UserDtoIn requestor1 = UserDtoIn.builder()
                .name("requestor1")
                .email("requesto1r@gmail.com")
                .build();

        UserDtoOut savedRequestor1 = userService.create(requestor1);

        UserDtoIn requestor2 = UserDtoIn.builder()
                .name("requestor2")
                .email("requestor2@gmail.com")
                .build();

        UserDtoOut savedRequestor2 = userService.create(requestor2);

        ItemRequestDtoIn itemRequest1 = new ItemRequestDtoIn("description");
        itemRequestService.create(itemRequest1, savedRequestor1.getId());
        ItemRequestDtoIn itemRequest2 = new ItemRequestDtoIn("description");
        itemRequestService.create(itemRequest2, savedRequestor1.getId());
        ItemRequestDtoIn itemRequest3 = new ItemRequestDtoIn("description");
        itemRequestService.create(itemRequest3, savedRequestor2.getId());

        assertThat(itemRequestService.getAllByRequestor(savedRequestor1.getId())).hasSize(2);
    }

    @Test
    void shouldGetAll() {
        UserDtoIn requestor1 = UserDtoIn.builder()
                .name("requestor1")
                .email("requesto1r@gmail.com")
                .build();

        UserDtoOut savedRequestor1 = userService.create(requestor1);

        UserDtoIn requestor2 = UserDtoIn.builder()
                .name("requestor2")
                .email("requestor2@gmail.com")
                .build();

        UserDtoOut savedRequestor2 = userService.create(requestor2);

        ItemRequestDtoIn itemRequest1 = new ItemRequestDtoIn("description");
        itemRequestService.create(itemRequest1, savedRequestor1.getId());
        ItemRequestDtoIn itemRequest2 = new ItemRequestDtoIn("description");
        itemRequestService.create(itemRequest2, savedRequestor1.getId());
        ItemRequestDtoIn itemRequest3 = new ItemRequestDtoIn("description");
        itemRequestService.create(itemRequest3, savedRequestor1.getId());

        assertThat(itemRequestService.getAll(savedRequestor2.getId(), 1, 10)).hasSize(2);
    }
}
