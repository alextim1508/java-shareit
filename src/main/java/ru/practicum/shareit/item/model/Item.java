package ru.practicum.shareit.item.model;

import lombok.*;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Item {

    private Integer id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private Boolean available;

    @NonNull
    private Integer ownerId;

    private Integer requestId;
}
