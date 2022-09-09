package ru.practicum.shareit.requests.service;

import org.mapstruct.*;
import ru.practicum.shareit.item.service.CommentMapper;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.requests.controller.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserService.class, CommentMapper.class})
public interface ItemRequestMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "requesterId", target = "requestor")
    ItemRequest fromDto(ItemRequestDto itemRequestDto, Integer requesterId);

    ItemRequestDto toDto(ItemRequest itemRequest);

    List<ItemRequestDto> toDto(List<ItemRequest> itemRequests);
}
