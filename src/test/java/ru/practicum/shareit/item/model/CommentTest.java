package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemBaseTest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void allArgsConstructorTest() {
        Comment comment = new Comment(this.comment.getId(), this.comment.getText(), item, booker, now);

        assertThat(comment.getId()).isEqualTo(this.comment.getId());
        assertThat(comment.getText()).isEqualTo(this.comment.getText());
        assertThat(comment.getItem()).isEqualTo(item);
        assertThat(comment.getAuthor()).isEqualTo(booker);
        assertThat(comment.getCreated()).isEqualTo(now);
    }

    @Test
    void allArgsConstructor_shouldThrowNotFoundExceptionWhenTextIsNull() {
        assertThatThrownBy(() -> {
            new Comment(this.comment.getId(), null, item, booker, now);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("text is marked non-null but is null");
    }

    @Test
    void allArgsConstructor_shouldThrowNotFoundExceptionWhenItemIsNull() {
        assertThatThrownBy(() -> {
            new Comment(this.comment.getId(), this.comment.getText(), null, booker, now);
            ;
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("item is marked non-null but is null");
    }

    @Test
    void allArgsConstructor_shouldThrowNotFoundExceptionWhenAuthorIsNull() {
        assertThatThrownBy(() -> {
            new Comment(this.comment.getId(), this.comment.getText(), item, null, now);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("author is marked non-null but is null");
    }

    @Test
    void builder_shouldReturnNotNull() {
        Comment.CommentBuilder builder = Comment.builder()
                .id(this.comment.getId())
                .text(this.comment.getText())
                .author(booker)
                .item(item)
                .created(now);

        assertThat(builder.toString()).contains(builder.getClass().getSimpleName());
        assertThat(builder.build()).isNotNull();
    }

    @Test
    void builder_shouldThrowNotFoundExceptionWhenTextIsNull() {
        assertThatThrownBy(() -> {
            Comment.builder()
                    .id(this.comment.getId())
                    .text(null)
                    .author(booker)
                    .item(item)
                    .created(now)
                    .build();

        }).isInstanceOf(NullPointerException.class)
        .hasMessage("text is marked non-null but is null");
    }

    @Test
    void builder_shouldThrowNotFoundExceptionWhenItemIsNull() {
        assertThatThrownBy(() -> {
            Comment.builder()
                    .id(this.comment.getId())
                    .text(this.comment.getText())
                    .author(booker)
                    .item(null)
                    .created(now)
                    .build();
        }).isInstanceOf(NullPointerException.class)
          .hasMessage("item is marked non-null but is null");
    }

    @Test
    void builder_shouldThrowNotFoundExceptionWhenAuthorIsNull() {
        assertThatThrownBy(() -> {
            Comment.builder()
                    .id(this.comment.getId())
                    .text(this.comment.getText())
                    .author(null)
                    .item(item)
                    .created(now)
                    .build();
        }).isInstanceOf(NullPointerException.class)
          .hasMessage("author is marked non-null but is null");
    }

    @Test
    void setName_shouldThrowNotFoundExceptionWhenTextIsNull() {
        Comment comment = new Comment(this.comment.getId(), this.comment.getText(), item, booker, now);

        assertThatThrownBy(() -> {
            comment.setText(null);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("text is marked non-null but is null");
    }

    @Test
    void setName_shouldThrowNotFoundExceptionWhenAuthorIsNull() {
        Comment comment = new Comment(this.comment.getId(), this.comment.getText(), item, booker, now);

        assertThatThrownBy(() -> {
            comment.setAuthor(null);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("author is marked non-null but is null");
    }

    @Test
    void setName_shouldThrowNotFoundExceptionWhenItemIsNull() {
        Comment comment = new Comment(this.comment.getId(), this.comment.getText(), item, booker, now);

        assertThatThrownBy(() -> {
            comment.setItem(null);
        }).isInstanceOf(NullPointerException.class)
                .hasMessage("item is marked non-null but is null");
    }

    @Test
    void toStringTest() {
        assertThat(comment.toString()).startsWith(comment.getClass().getSimpleName());
    }

}