package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemBaseTest;


import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest extends ItemBaseTest {

    @Test
    void equalsAndHashCodeTest() {
        Comment x = new Comment(1, "comment", item, booker, now);
        Comment y = new Comment(2, "comment", item, booker, now);

        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }


    @Test
    void equalsTest() {
        assertThat(comment.equals(comment)).isTrue();
        assertThat(comment.equals(null)).isFalse();
        assertThat(comment.equals(new Object())).isFalse();
    }

    @Test
    void noArgsConstructorTest() {
        Comment comment = new Comment();
        comment.setId(this.comment.getId());
        comment.setText(this.comment.getText());
        comment.setItem(item);
        comment.setAuthor(booker);
        comment.onCreate();

        assertThat(comment.getId()).isEqualTo(this.comment.getId());
        assertThat(comment.getText()).isEqualTo(this.comment.getText());
        assertThat(comment.getItem()).isEqualTo(item);
        assertThat(comment.getAuthor()).isEqualTo(booker);
        assertThat(comment.getCreated()).isEqualTo(now);
    }


    @Test
    void toStringTest() {
        assertThat(comment.toString()).startsWith(comment.getClass().getSimpleName());
    }
}