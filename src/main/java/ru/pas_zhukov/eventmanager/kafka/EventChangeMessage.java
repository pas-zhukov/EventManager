package ru.pas_zhukov.eventmanager.kafka;

import ru.pas_zhukov.eventmanager.model.EventStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class EventChangeMessage {
    private List<Long> users;
    private Long eventId;
    private Long ownerId;
    private Long changedByUserId;

    private MessageType messageType;

    private FieldChange<String> name;
    private FieldChange<Integer> maxPlaces;
    private FieldChange<Date> date;
    private FieldChange<BigDecimal> cost;
    private FieldChange<Integer> duration;
    private FieldChange<Long> locationId;
    private FieldChange<EventStatus> status;

    public EventChangeMessage() {
    }

    public EventChangeMessage(Long changedByUserId, FieldChange<BigDecimal> cost, FieldChange<Date> date, FieldChange<Integer> duration, Long eventId, FieldChange<Long> locationId, FieldChange<Integer> maxPlaces, FieldChange<String> name, Long ownerId, FieldChange<EventStatus> status, List<Long> users, MessageType messageType) {
        this.changedByUserId = changedByUserId;
        this.cost = cost;
        this.date = date;
        this.duration = duration;
        this.eventId = eventId;
        this.locationId = locationId;
        this.maxPlaces = maxPlaces;
        this.name = name;
        this.ownerId = ownerId;
        this.status = status;
        this.users = users;
        this.messageType = messageType;
    }

    public static EventChangeMessageBuilder builder() {
        return new EventChangeMessageBuilder();
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getChangedByUserId() {
        return changedByUserId;
    }

    public void setChangedByUserId(Long changedByUserId) {
        this.changedByUserId = changedByUserId;
    }

    public FieldChange<BigDecimal> getCost() {
        return cost;
    }

    public void setCost(FieldChange<BigDecimal> cost) {
        this.cost = cost;
    }

    public FieldChange<Date> getDate() {
        return date;
    }

    public void setDate(FieldChange<Date> date) {
        this.date = date;
    }

    public FieldChange<Integer> getDuration() {
        return duration;
    }

    public void setDuration(FieldChange<Integer> duration) {
        this.duration = duration;
    }

    public FieldChange<Long> getLocationId() {
        return locationId;
    }

    public void setLocationId(FieldChange<Long> locationId) {
        this.locationId = locationId;
    }

    public FieldChange<Integer> getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(FieldChange<Integer> maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public FieldChange<String> getName() {
        return name;
    }

    public void setName(FieldChange<String> name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public FieldChange<EventStatus> getStatus() {
        return status;
    }

    public void setStatus(FieldChange<EventStatus> status) {
        this.status = status;
    }

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }
}
