package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.controller.ShortBookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Integer id;

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

    private ShortBookingDto nextBooking;

    private ShortBookingDto lastBooking;

    private List<CommentDto> comments;
}
