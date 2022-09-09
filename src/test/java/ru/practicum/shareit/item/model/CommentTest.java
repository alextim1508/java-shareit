package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {

    User owner, author;
    Item item;
    LocalDateTime now;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1)
                .name("owner")
                .email("owner@gmail.com")
                .build();

        item = Item.builder()
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();

        author = User.builder()
                .id(2)
                .name("author")
                .email("author@gmail.com")
                .build();
        now = LocalDateTime.now().withNano(0);
    }

    @Test
    void testEquals_Symmetric() {
        Comment x = new Comment(1, "comment", item, author, now);
        Comment y = new Comment(2, "comment", item, author, now);


        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void noArgsConstructorTest() {
        int id = 1;
        String text = "comment";

        Comment comment = new Comment();
        comment.setId(id);
        comment.setText(text);
        comment.setItem(item);
        comment.setAuthor(author);
        comment.onCreate();

        assertThat(comment.getId()).isEqualTo(id);
        assertThat(comment.getText()).isEqualTo(text);
        assertThat(comment.getItem()).isEqualTo(item);
        assertThat(comment.getAuthor()).isEqualTo(author);
        assertThat(comment.getCreated()).isEqualTo(now);

    }

    @Test
    void toStringTest() {
        int id = 1;
        String text = "comment";

        Comment comment = Comment.builder()
                .id(id)
                .text(text)
                .item(item)
                .author(author)
                .created(now)
                .build();

        assertThat(comment.toString()).startsWith(comment.getClass().getSimpleName());
    }
}
