package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDtoOut extends ItemRequestDtoOutAbs {

    private Integer id;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;

    private List<ItemDtoOut> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequestDtoOut that = (ItemRequestDtoOut) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(description, that.description) &&
                Objects.equals(created, that.created) &&
                Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, created, items);
    }
}
