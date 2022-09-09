package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private LocalDateTime startDate;
    @NonNull
    private LocalDateTime endDate;

    @NonNull
    @ManyToOne()
    private Item item;

    @NonNull
    @ManyToOne()
    private User booker;

    @NonNull
    @Enumerated(value = EnumType.STRING)
    private BookingStatus status;
}
