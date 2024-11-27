package ru.pas_zhukov.eventmanager.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "locations")
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<EventEntity> locationEvents;

    public LocationEntity() {
    }

    public LocationEntity(Long id, String name, String address, Integer capacity, String description, List<EventEntity> locationEvents) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.description = description;
        this.locationEvents = locationEvents;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<EventEntity> getLocationEvents() {
        return locationEvents;
    }

    public void setLocationEvents(List<EventEntity> locationEvents) {
        this.locationEvents = locationEvents;
    }
}
