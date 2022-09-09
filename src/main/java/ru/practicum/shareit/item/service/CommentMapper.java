package ru.practicum.shareit.item.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.controller.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemFactory.class, UserService.class})
public interface CommentMapper {

    @Mapping(source = "author.name", target = "authorName")
    CommentDto toDto(Comment comment);

    List<CommentDto> toDto(List<Comment> comment);

    @Mapping(source = "itemId", target = "item")
    @Mapping(source = "userId", target = "author")
    Comment fromDto(CommentDto commentDto, Integer itemId, Integer userId);
}
