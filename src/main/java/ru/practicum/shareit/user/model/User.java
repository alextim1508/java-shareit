package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(schema = "public", name = "user")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String name;

    @NonNull
    private String email;

    public User(Integer id) {
        this.id = id;
    }
}
