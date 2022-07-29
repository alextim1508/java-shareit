package ru.practicum.shareit.requests.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequest {

    private Integer id;

    private String description;

    private Integer requestorId;

    private LocalDateTime created;
}
