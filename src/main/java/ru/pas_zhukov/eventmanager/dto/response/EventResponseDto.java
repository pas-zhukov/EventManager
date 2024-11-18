package ru.pas_zhukov.eventmanager.dto.response;

import ru.pas_zhukov.eventmanager.model.EventStatus;
import ru.pas_zhukov.eventmanager.model.Location;
import ru.pas_zhukov.eventmanager.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventResponseDto {
    private Long id;
    private String name;
    private User owner;
    private Integer maxPlaces;
    private Integer occupiedPlaces;
    private LocalDateTime date;
    private BigDecimal cost;
    private Integer duration;
    private Location location;
    private EventStatus status;

    public EventResponseDto() {}

    public EventResponseDto(Long id, String name, User owner, Integer maxPlaces, Integer occupiedPlaces, LocalDateTime date, BigDecimal cost, Integer duration, Location location, EventStatus status) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.maxPlaces = maxPlaces;
        this.occupiedPlaces = occupiedPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.location = location;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public Integer getOccupiedPlaces() {
        return occupiedPlaces;
    }

    public void setOccupiedPlaces(Integer occupiedPlaces) {
        this.occupiedPlaces = occupiedPlaces;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }
}
