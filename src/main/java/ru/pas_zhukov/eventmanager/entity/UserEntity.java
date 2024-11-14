package ru.pas_zhukov.eventmanager.entity;

import jakarta.persistence.*;
import ru.pas_zhukov.eventmanager.model.UserRole;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "passwordHash")
    private String passwordHash;

    @Column(name = "age")
    private Integer age;

    @Column(name = "role")
    private UserRole role;

    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    private List<EventEntity> ownedEvents = new ArrayList<>();

    public UserEntity() {
    }

    public UserEntity(Long id, String login, String passwordHash, Integer age, UserRole role, List<EventEntity> ownedEvents) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
        this.age = age;
        this.role = role;
        this.ownedEvents = ownedEvents;
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

    public List<EventEntity> getOwnedEvents() {
        return ownedEvents;
    }

    public void setOwnedEvents(List<EventEntity> ownedEvents) {
        this.ownedEvents = ownedEvents;
    }
}
