package ru.pas_zhukov.eventmanager.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SignUpRequestDto {

    @NotBlank
    @Size(min = 5, max = 20)
    private String login;

    @NotBlank
    @Size(min = 5)
    private String password;

    @NotNull
    @Min(18)
    private Integer age;

    public SignUpRequestDto() {
    }

    public SignUpRequestDto(String login, String password, Integer age) {
        this.login = login;
        this.password = password;
        this.age = age;
    }

    public @NotBlank @Size(min = 5, max = 20) String getLogin() {
        return login;
    }

    public void setLogin(@NotBlank @Size(min = 5, max = 20) String login) {
        this.login = login;
    }

    public @NotBlank @Size(min = 5) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 5) String password) {
        this.password = password;
    }

    public @NotNull Integer getAge() {
        return age;
    }

    public void setAge(@NotNull Integer age) {
        this.age = age;
    }
}
