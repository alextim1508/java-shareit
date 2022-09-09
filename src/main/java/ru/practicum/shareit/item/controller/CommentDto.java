package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CommentDto {

    private Integer id;

    @NotEmpty
    @NotBlank
    @NotNull
    private String text;

    private String authorName;

    private String created;

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getCreated().toString());
    }

    public static Comment toComment(CommentDto commentDto, int authorId, int itemId) {
        return new Comment(commentDto.getText(), new Item(itemId), new User(authorId));
    }
}
