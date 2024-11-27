package ru.pas_zhukov.eventmanager.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Date;

public class EventCreateRequestDto {
    @NotBlank
    private String name;
    @NotNull
    private Integer maxPlaces;
    @NotNull
    private Date date;
    @NotNull
    @PositiveOrZero
    private BigDecimal cost;
    @NotNull
    @Min(30)
    private Integer duration;
    @NotNull
    private Long locationId;

    public EventCreateRequestDto() {
    }

    public EventCreateRequestDto(String name, Integer maxPlaces, Date date, BigDecimal cost, Integer duration, Long locationId) {
        this.name = name;
        this.maxPlaces = maxPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.locationId = locationId;
    }

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public @NotNull Integer getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(@NotNull Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public @NotNull Date getDate() {
        return date;
    }

    public void setDate(@NotNull Date date) {
        this.date = date;
    }

    public @NotNull @PositiveOrZero BigDecimal getCost() {
        return cost;
    }

    public void setCost(@NotNull @PositiveOrZero BigDecimal cost) {
        this.cost = cost;
    }

    public @NotNull @Min(30) Integer getDuration() {
        return duration;
    }

    public void setDuration(@NotNull @Min(30) Integer duration) {
        this.duration = duration;
    }

    public @NotNull Long getLocationId() {
        return locationId;
    }

    public void setLocationId(@NotNull Long locationId) {
        this.locationId = locationId;
    }
}
