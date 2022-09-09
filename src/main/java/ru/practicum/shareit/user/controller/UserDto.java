package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.validator.NullAllowed;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

@Data
@AllArgsConstructor
public class UserDto {

    private Integer id;

    @NotEmpty(groups = {Default.class})
    @NotBlank(groups = {Default.class})
    @NotNull(groups = {Default.class})
    private String name;

    @Email(groups = {NullAllowed.class, Default.class})
    @NotNull(groups = {Default.class})
    private String email;

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto dto) {
        return new User(
                dto.getName(),
                dto.getEmail()
        );
    }
}
