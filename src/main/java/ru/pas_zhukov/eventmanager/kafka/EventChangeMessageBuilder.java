package ru.pas_zhukov.eventmanager.kafka;

import ru.pas_zhukov.eventmanager.model.EventStatus;
import ru.pas_zhukov.eventmanager.model.User;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class EventChangeMessageBuilder {

    private final EventChangeMessage event = new EventChangeMessage();

    public EventChangeMessage build() {
        if (event.getUsers() == null || event.getOwnerId() == null || event.getEventId() == null) {
            throw new IllegalStateException("EventId, ownerId and users must be specified");
        }
        return event;
    }

    public EventChangeMessageBuilder withChangedByUserId(Long userId) {
        event.setChangedByUserId(userId);
        return this;
    }

    public EventChangeMessageBuilder withEventId(Long eventId) {
        event.setEventId(eventId);
        return this;
    }

    public EventChangeMessageBuilder withUsers(List<User> users) {
        event.setUsers(users.stream().mapToLong(User::getId).boxed().toList());
        return this;
    }

    public EventChangeMessageBuilder withUsersIds(List<Long> userIds) {
        event.setUsers(userIds);
        return this;
    }

    public EventChangeMessageBuilder withOwner(User owner) {
        event.setOwnerId(owner.getId());
        return this;
    }

    public EventChangeMessageBuilder withOwnerId(Long ownerId) {
        event.setOwnerId(ownerId);
        return this;
    }

    public EventChangeMessageBuilder withStatusChange(EventStatus oldStatus, EventStatus newStatus) {
        event.setStatus(new FieldChange<>(oldStatus, newStatus));
        return this;
    }

    public EventChangeMessageBuilder withNameChange(String oldName, String newName) {
        event.setName(new FieldChange<>(oldName, newName));
        return this;
    }

    public EventChangeMessageBuilder withMaxPlacesChange(Integer oldPlaces, Integer newPlaces) {
        event.setMaxPlaces(new FieldChange<>(oldPlaces, newPlaces));
        return this;
    }

    public EventChangeMessageBuilder withDateChange(Date oldDate, Date newDate) {
        event.setDate(new FieldChange<>(oldDate, newDate));
        return this;
    }

    public EventChangeMessageBuilder withCostChange(BigDecimal oldCost, BigDecimal newCost) {
        event.setCost(new FieldChange<>(oldCost, newCost));
        return this;
    }

    public EventChangeMessageBuilder withDurationChange(Integer oldDuration, Integer newDuration) {
        event.setDuration(new FieldChange<>(oldDuration, newDuration));
        return this;
    }

    public EventChangeMessageBuilder withLocationIdChange(Long oldLocation, Long newLocation) {
        event.setLocationId(new FieldChange<>(oldLocation, newLocation));
        return this;
    }

    public EventChangeMessageBuilder withMessageType(MessageType messageType) {
        event.setMessageType(messageType);
        return this;
    }

}
