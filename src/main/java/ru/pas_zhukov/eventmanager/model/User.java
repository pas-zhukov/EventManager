package ru.pas_zhukov.eventmanager.model;


import java.util.Objects;


public class User {
    private Long id;
    private String login;
    private String passwordHash;
    private Integer age;
    private UserRole role;

    public User() {
    }

    public User(Long id, String login, String passwordHash, Integer age, UserRole role) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
        this.age = age;
        this.role = role;
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

