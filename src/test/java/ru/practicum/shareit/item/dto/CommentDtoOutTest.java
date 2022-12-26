package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.ItemBaseTest;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentDtoOutTest extends ItemBaseTest {

    @Autowired
    private JacksonTester<CommentDtoOut> jacksonTester;

    @Test
    void toItemDto() throws IOException {
        JsonContent<CommentDtoOut> result = jacksonTester.write(commentDtoOut);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(commentDtoOut.getId());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentDtoOut.getText());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(commentDtoOut.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(now
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    void equalsAndHashCodeTest() {
        CommentDtoOut x = CommentDtoOut.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();

        CommentDtoOut y = CommentDtoOut.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();

        assertThat(x.equals(y) && y.equals(x)).isTrue();
        assertThat(x.hashCode() == y.hashCode()).isTrue();
    }

    @Test
    void equalsTest() {
        CommentDtoOut x = CommentDtoOut.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();

        assertThat(x.equals(x)).isTrue();
        assertThat(x.equals(null)).isFalse();
        assertThat(x.equals(new Object())).isFalse();
    }

    @Test
    void noArgsConstructorTest() {
        CommentDtoOut commentDto = new CommentDtoOut();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());

        assertThat(commentDto.getId()).isEqualTo(comment.getId());
        assertThat(commentDto.getText()).isEqualTo(comment.getText());
        assertThat(commentDto.getAuthorName()).isEqualTo(comment.getAuthor().getName());
        assertThat(commentDto.getCreated()).isEqualTo(comment.getCreated());
    }

    @Test
    void allArgsConstructorTest() {
        CommentDtoOut commentDto = new CommentDtoOut(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());

        assertThat(commentDto.getId()).isEqualTo(comment.getId());
        assertThat(commentDto.getText()).isEqualTo(comment.getText());
        assertThat(commentDto.getAuthorName()).isEqualTo(comment.getAuthor().getName());
        assertThat(commentDto.getCreated()).isEqualTo(comment.getCreated());
    }

    @Test
    void toStringTest() {
        assertThat(commentDtoOut.toString()).startsWith(commentDtoOut.getClass().getSimpleName());
    }
}
