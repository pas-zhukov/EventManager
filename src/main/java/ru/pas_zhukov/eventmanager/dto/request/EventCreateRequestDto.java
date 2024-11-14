package ru.pas_zhukov.eventmanager.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventCreateRequestDto {
    @NotBlank
    private String name;
    @NotNull
    private Integer maxPlaces;
    @NotNull
    private LocalDateTime date;
    @NotNull
    @PositiveOrZero
    private BigDecimal cost;
    @NotNull
    @Min(30)
    private Integer duration;
    @NotNull
    private Long locationId;
}
