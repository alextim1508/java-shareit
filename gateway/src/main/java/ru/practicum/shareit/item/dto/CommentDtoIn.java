package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDtoIn {

    @NotBlank
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentDtoIn that = (CommentDtoIn) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
