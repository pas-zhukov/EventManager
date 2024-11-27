package ru.pas_zhukov.eventmanager.model;

public class Registration {
    private Long id;
    private User user;
    private Event event;

    public Registration() {
    }

    public Registration(Long id, User user, Event event) {
        this.event = event;
        this.id = id;
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
