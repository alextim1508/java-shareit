package ru.practicum.shareit.user.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDtoIn {

    private String name;

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
