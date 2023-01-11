package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.util.validation.Create;
import ru.practicum.shareit.util.validation.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDtoIn {

    @NotBlank(groups = {Create.class})
    @Pattern(regexp = "^(?!\\s*$).+", message = "can not be blank, but can be null", groups = {Update.class})
    private String name;

    @NotBlank(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
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
