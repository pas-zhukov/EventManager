package ru.pas_zhukov.eventmanager.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class EventUpdateRequestDto {
    private String name;
    private Integer maxPlaces;
    private Date date;
    @PositiveOrZero
    private BigDecimal cost;
    @Min(30)
    private Integer duration;
    private Long locationId;

    public EventUpdateRequestDto() {
    }

    public EventUpdateRequestDto(BigDecimal cost, Date date, Integer duration, Long locationId, Integer maxPlaces, String name) {
        this.cost = cost;
        this.date = date;
        this.duration = duration;
        this.locationId = locationId;
        this.maxPlaces = maxPlaces;
        this.name = name;
    }

    public @PositiveOrZero BigDecimal getCost() {
        return cost;
    }

    public void setCost(@PositiveOrZero BigDecimal cost) {
        this.cost = cost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public @Min(30) Integer getDuration() {
        return duration;
    }

    public void setDuration(@Min(30) Integer duration) {
        this.duration = duration;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
