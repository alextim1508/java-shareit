package ru.practicum.shareit.user.service;

import org.mapstruct.*;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserDto userDto, @MappingTarget User user);

    List<UserDto> toDto(List<User> users);

    User fromDto(UserDto userDto);

    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    UserDto toDto(User user);
}
