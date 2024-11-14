package ru.pas_zhukov.eventmanager.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Event {
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
}
