package ru.pas_zhukov.eventmanager.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import ru.pas_zhukov.eventmanager.model.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventSearchRequestDto {
    private String name;
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

    public EventSearchRequestDto() {
    }

    public EventSearchRequestDto(String name, Integer placesMin, Integer placesMax, LocalDateTime dateStartAfter, LocalDateTime dateStartBefore, BigDecimal costMin, BigDecimal costMax, Integer durationMin, Integer durationMax, Long locationId, EventStatus eventStatus) {
        this.name = name;
        this.placesMin = placesMin;
        this.placesMax = placesMax;
        this.dateStartAfter = dateStartAfter;
        this.dateStartBefore = dateStartBefore;
        this.costMin = costMin;
        this.costMax = costMax;
        this.durationMin = durationMin;
        this.durationMax = durationMax;
        this.locationId = locationId;
        this.eventStatus = eventStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPlacesMin() {
        return placesMin;
    }

    public void setPlacesMin(Integer placesMin) {
        this.placesMin = placesMin;
    }

    public Integer getPlacesMax() {
        return placesMax;
    }

    public void setPlacesMax(Integer placesMax) {
        this.placesMax = placesMax;
    }

    public LocalDateTime getDateStartAfter() {
        return dateStartAfter;
    }

    public void setDateStartAfter(LocalDateTime dateStartAfter) {
        this.dateStartAfter = dateStartAfter;
    }

    public LocalDateTime getDateStartBefore() {
        return dateStartBefore;
    }

    public void setDateStartBefore(LocalDateTime dateStartBefore) {
        this.dateStartBefore = dateStartBefore;
    }

    public @PositiveOrZero BigDecimal getCostMin() {
        return costMin;
    }

    public void setCostMin(@PositiveOrZero BigDecimal costMin) {
        this.costMin = costMin;
    }

    public @PositiveOrZero BigDecimal getCostMax() {
        return costMax;
    }

    public void setCostMax(@PositiveOrZero BigDecimal costMax) {
        this.costMax = costMax;
    }

    public @Min(30) Integer getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(@Min(30) Integer durationMin) {
        this.durationMin = durationMin;
    }

    public @Min(30) Integer getDurationMax() {
        return durationMax;
    }

    public void setDurationMax(@Min(30) Integer durationMax) {
        this.durationMax = durationMax;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }
}
