package ru.pas_zhukov.eventmanager.entity;

import jakarta.persistence.*;
import ru.pas_zhukov.eventmanager.model.EventStatus;
import ru.pas_zhukov.eventmanager.model.Location;
import ru.pas_zhukov.eventmanager.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String name;

    @JoinColumn(name = "owner_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity owner;

    @Column(name = "max_places", nullable = false)
    private Integer maxPlaces;

    @Column(name = "occupied_places")
    private Integer occupiedPlaces;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @Column(name = "status", nullable = false)
    private EventStatus status;

    public EventEntity() {
    }

    public EventEntity(Long id, String name, UserEntity owner, Integer maxPlaces, Integer occupiedPlaces, Date date, BigDecimal cost, Integer duration, LocationEntity location, EventStatus status) {
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

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }
}
