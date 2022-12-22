package ru.practicum.shareit.user.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    private Integer id;

    @NonNull
    private String name;

    @NonNull
    private String email;
}
