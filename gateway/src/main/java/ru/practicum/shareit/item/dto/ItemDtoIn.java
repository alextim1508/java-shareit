package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.util.validation.Create;
import ru.practicum.shareit.util.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDtoIn {

    @NotBlank(groups = {Create.class})
    @Pattern(regexp = "^(?!\\s*$).+", message = "can not be blank, but can be null", groups = {Update.class})
    private String name;

    @NotBlank(groups = {Create.class})
    @Pattern(regexp = "^(?!\\s*$).+", message = "can not be blank, but can be null", groups = {Update.class})
    private String description;

    @NotNull(groups = {Create.class})
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
