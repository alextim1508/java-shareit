package ru.practicum.shareit.requests.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.OffsetLimitPageable;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemRequestServiceTest {

    @MockBean
    ItemRequestRepository itemRequestRepository;
    @MockBean
    UserService userService;

    @Autowired
    ItemRequestService itemRequestService;

    User requestor;
    ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        doNothing().when(userService).existenceCheck(anyInt());

        requestor = new User(1, "user1", "user1@gmail.com");
        itemRequest = new ItemRequest("description", requestor);
    }

    @Test
    void create_shouldInvokeRepositoryAndReturnTheSame() {
        when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);

        ItemRequest savedItemRequest = itemRequestService.create(itemRequest);

        assertThat(savedItemRequest).isEqualTo(itemRequest);

        verify(itemRequestRepository, times(1)).save(itemRequest);
    }

    @Test
    void getById_shouldThrowNotFoundExceptionWhenRepositoryReturnsEmpty() {
        when(itemRequestRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            itemRequestService.getById(1, 1);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getById_shouldInvokeRepositoryAndReturnTheSame() {
        when(itemRequestRepository.findById(anyInt()))
                .thenReturn(Optional.of(itemRequest));

        ItemRequest itemRequestById = itemRequestService.getById(1, 1);

        verify(itemRequestRepository, times(1)).findById(1);
        assertThat(itemRequestById).isEqualTo(itemRequest);
    }

    @Test
    void getAllByRequestor_shouldInvokeRepositoryAndReturnTheSame() {
        when(itemRequestRepository.getItemRequestByRequestor(anyInt()))
                .thenReturn(Arrays.asList(itemRequest));

        List<ItemRequest> itemRequests = itemRequestService.getAllByRequestor(1);

        verify(itemRequestRepository, times(1)).getItemRequestByRequestor(eq(1));
        assertThat(itemRequests).isNotNull().isNotEmpty().containsAll(Arrays.asList(itemRequest));
    }

    @Test
    void getAll_shouldInvokeRepositoryAndReturnTheSame() {
        when(itemRequestRepository.findAllByRequestorIdNot(anyInt(), any(OffsetLimitPageable.class)))
                .thenReturn(new PageImpl(Arrays.asList(itemRequest)));

        List<ItemRequest> itemRequests = itemRequestService.getAll(1, 0, 10);

        verify(itemRequestRepository, times(1)).findAllByRequestorIdNot(eq(1), any(OffsetLimitPageable.class));
        assertThat(itemRequests).isNotNull().isNotEmpty().containsAll(Arrays.asList(itemRequest));
    }

}
