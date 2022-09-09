package ru.practicum.shareit.item.controller;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortItemDto {

    private Integer id;

    private String name;

    private Integer ownerId;
}