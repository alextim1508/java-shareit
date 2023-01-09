package ru.practicum.shareit.request.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDtoIn {

    @NotBlank
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequestDtoIn that = (ItemRequestDtoIn) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
