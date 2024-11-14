package ru.pas_zhukov.eventmanager.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import ru.pas_zhukov.eventmanager.model.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventSearchRequestDto {
    private Integer placesMin;
    private Integer placesMax;
    private LocalDateTime dateStartAfter;
    private LocalDateTime dateStartBefore;
    @PositiveOrZero
    private BigDecimal costMin;
    @PositiveOrZero
    private BigDecimal costMax;
    @Min(30)
    private Integer durationMin;
    @Min(30)
    private Integer durationMax;
    private Long locationId;
    private EventStatus eventStatus;
}
