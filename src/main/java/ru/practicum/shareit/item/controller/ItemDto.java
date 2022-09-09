package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
public class ItemDto {

    private Integer id;

    @NotEmpty
    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotEmpty
    @NotNull
    private String description;

    @NotNull
    private Boolean available;

    private Integer requestId; //todo!!


    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null);
    }

    public static Item toItem(ItemDto dto, int ownerId) {
        Item item = new Item(
                dto.getName(),
                dto.getDescription(),
                dto.getAvailable(),
                new User(ownerId)
        );

        if (dto.getRequestId() != null)
            item.setRequest(new ItemRequest(dto.getRequestId()));

        return item;
    }
}
