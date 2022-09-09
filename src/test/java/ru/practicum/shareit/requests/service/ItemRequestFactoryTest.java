package ru.practicum.shareit.requests.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemRequestFactoryTest {

    @Autowired
    ItemRequestFactory wrapper;

    @MockBean
    ItemRequestRepository itemRequestRepository;

    @Test
    void loadTest() {
        assertThat(wrapper).isNotNull();
    }

    @Test
    void get_shouldInvokeServiceAndReturnTheSame() {
        User requestor = new User(1, "requestor", "requestor@gmail.com");
        ItemRequest itemRequest = new ItemRequest("description", requestor);

        when(itemRequestRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(itemRequest));

        assertThat(wrapper.getById(1)).isEqualTo(itemRequest);
    }

    @Test
    void get_shouldThrowException() {
        when(itemRequestRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            wrapper.getById(1);
        }).isInstanceOf(NotFoundException.class)
          .hasMessage("Request not found");
    }
}
