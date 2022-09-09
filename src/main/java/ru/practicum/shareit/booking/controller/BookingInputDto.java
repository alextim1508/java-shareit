package ru.practicum.shareit.booking.controller;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.shareit.util.validator.EarlierThan;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@EarlierThan(value = "startDate", earlierThan = "endDate")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class BookingInputDto {

    private Integer id;

    @NotNull
    @NonNull
    private Integer itemId;

    @Future
    @NotNull
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("start")
    private LocalDateTime startDate;

    @Future
    @NotNull
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("end")
    private LocalDateTime endDate;
}
