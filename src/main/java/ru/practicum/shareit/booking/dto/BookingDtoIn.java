package ru.practicum.shareit.booking.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.shareit.util.validator.EarlierThan;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@EarlierThan(value = "startDate", earlierThan = "endDate")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoIn {

    @NotNull
    private Integer itemId;

    @Future
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("start")
    private LocalDateTime startDate;

    @Future
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("end")
    private LocalDateTime endDate;
}
