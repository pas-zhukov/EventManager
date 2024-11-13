package ru.pas_zhukov.eventmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignInRequestDto {
    @NotBlank
    @Size(min = 5, max = 20)
    private String login;

    @NotBlank
    @Size(min = 5)
    private String password;

    public SignInRequestDto() {
    }

    public SignInRequestDto(String login, String password) {
        this.login = login;
        this.password = password;
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
}
