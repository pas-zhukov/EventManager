package ru.pas_zhukov.eventmanager.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pas_zhukov.eventmanager.converter.UserConverter;
import ru.pas_zhukov.eventmanager.dto.request.SignInRequestDto;
import ru.pas_zhukov.eventmanager.dto.request.SignUpRequestDto;
import ru.pas_zhukov.eventmanager.dto.response.JwtResponseDto;
import ru.pas_zhukov.eventmanager.dto.response.UserResponseDto;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.security.AuthenticationService;
import ru.pas_zhukov.eventmanager.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;
    private final AuthenticationService authenticationService;

    public UserController(UserService userService, UserConverter userConverter, AuthenticationService authenticationService) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> signUpUser(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        User user = userService.registerUser(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userConverter.toResponseDto(user));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtResponseDto> authenticate(@Valid @RequestBody SignInRequestDto signInDto) {
        String token = authenticationService.authenticateUser(signInDto);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(userConverter.toResponseDto(userService.getUserById(id)));
    }
}
