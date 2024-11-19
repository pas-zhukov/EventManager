package ru.pas_zhukov.eventmanager.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventUpdateRequestDto {
    private String name;
    private Integer maxPlaces;
    private LocalDateTime date;
    @PositiveOrZero
    private BigDecimal cost;
    @Min(30)
    private Integer duration;
    private Long locationId;
}
