package ru.pas_zhukov.eventmanager.model;


import java.util.List;
import java.util.Objects;


public class User {
    private Long id;
    private String login;
    private String passwordHash;
    private Integer age;
    private UserRole role;
    private List<Event> ownedEvents;
    private List<Registration> registrations;

    public User() {
    }

    public User(Long id, String login, String passwordHash, Integer age, UserRole role, List<Event> ownedEvents, List<Registration> registrations) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
        this.age = age;
        this.role = role;
        this.ownedEvents = ownedEvents;
        this.registrations = registrations;
    }

    public User(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<Event> getOwnedEvents() {
        return ownedEvents;
    }

    public void setOwnedEvents(List<Event> ownedEvents) {
        this.ownedEvents = ownedEvents;
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(login, user.login) && Objects.equals(passwordHash, user.passwordHash) && Objects.equals(age, user.age) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, passwordHash, age, role);
    }
}

