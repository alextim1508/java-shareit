package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemFactoryTest {

    @Autowired
    ItemFactory wrapper;

    @MockBean
    ItemRepository itemRepository;

    @Test
    void get_shouldInvokeServiceAndReturnTheSame() {
        int id = 1;
        when(itemRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(Item.builder()
                        .id(id)
                        .name("name")
                        .description("desc")
                        .available(true)
                        .owner(new User(1, "user", "user@gmail.com"))
                        .build()));

        Item item = wrapper.get(id);
        assertThat(item.getId()).isEqualTo(id);
    }
}
