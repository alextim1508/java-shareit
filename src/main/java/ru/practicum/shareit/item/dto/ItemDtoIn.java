package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDtoIn {

    @NotBlank
    @NotEmpty
    @NotNull
    private String name;

    @NotBlank
    @NotEmpty
    @NotNull
    private String description;

    @NotNull
    private Boolean available;

    private Integer requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDtoIn itemDtoIn = (ItemDtoIn) o;
        return Objects.equals(name, itemDtoIn.name) &&
                Objects.equals(description, itemDtoIn.description) &&
                Objects.equals(available, itemDtoIn.available) &&
                Objects.equals(requestId, itemDtoIn.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, available, requestId);
    }
}
