package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.util.validator.NullAllowed;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDtoIn {

    @NotEmpty(groups = {Default.class})
    @NotBlank(groups = {Default.class})
    @NotNull(groups = {Default.class})
    private String name;

    @Email(groups = {NullAllowed.class, Default.class})
    @NotNull(groups = {Default.class})
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDtoIn userDtoIn = (UserDtoIn) o;
        return Objects.equals(name, userDtoIn.name) && Objects.equals(email, userDtoIn.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
