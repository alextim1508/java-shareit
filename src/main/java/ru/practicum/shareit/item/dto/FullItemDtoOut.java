package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.ShortBookingDtoOut;

import java.util.List;
import java.util.Objects;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullItemDtoOut extends ItemDtoOutAbs {

    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private Integer requestId;

    private ShortBookingDtoOut nextBooking;

    private ShortBookingDtoOut lastBooking;

    private List<CommentDtoOut> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullItemDtoOut that = (FullItemDtoOut) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(available, that.available) &&
                Objects.equals(requestId, that.requestId) &&
                Objects.equals(nextBooking, that.nextBooking) &&
                Objects.equals(lastBooking, that.lastBooking) &&
                Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available, requestId, nextBooking, lastBooking, comments);
    }
}
