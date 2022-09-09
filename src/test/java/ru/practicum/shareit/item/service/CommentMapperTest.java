package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.item.controller.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentMapperTest {

    @Autowired
    CommentMapper mapper;

    @MockBean
    ItemFactory itemFactory;

    @MockBean
    UserService userService;


    User owner, commentator;
    Item item;
    Comment comment;
    CommentDto commentDto;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        owner = new User(1, "user", "user@gmail.com");
        item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        commentator = new User(1, "commentator", "commentator@gmail.com");

        comment = new Comment(1, "comment", item, commentator, now);

        commentDto = new CommentDto(1, comment.getText(), commentator.getName(), now);
    }


    @Test
    void toDto_shouldInvokeServiceAndReturnTheSame() {
        assertThat(mapper.toDto(comment)).isEqualTo(commentDto);
    }

    @Test
    void toDto_shouldInvokeServiceAndReturnNull() {
        Comment comment = null;
        assertThat(mapper.toDto(comment)).isNull();
    }

    @Test
    void toDtoList_shouldInvokeServiceAndReturnTheSame() {
        assertThat(mapper.toDto(List.of(comment))).isEqualTo(List.of(commentDto));
    }

    @Test
    void toDtoList_shouldInvokeServiceAndReturnNull() {
        List<Comment> comment = null;
        assertThat(mapper.toDto(comment)).isNull();
    }

    @Test
    void fromDto_shouldInvokeServiceAndReturnTheSame() {
        when(itemFactory.get(anyInt())).thenReturn(item);
        when(userService.getById(anyInt())).thenReturn(commentator);

        LocalDateTime now = LocalDateTime.now();
        CommentDto commentDto = new CommentDto("comment");
        Comment expected = new Comment(1, "comment", item, commentator, now);

        assertThat(mapper.fromDto(commentDto, 1, 1)).isEqualTo(expected);
    }

    @Test
    void fromDto_shouldInvokeServiceAndReturnNull() {
        assertThat(mapper.fromDto(null, null, null)).isNull();
    }
}
