package ru.pas_zhukov.eventmanager.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.pas_zhukov.eventmanager.dto.request.SignInRequestDto;
import ru.pas_zhukov.eventmanager.model.User;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            JwtTokenManager jwtTokenManager
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    public String authenticateUser(SignInRequestDto signInRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getLogin(),
                        signInRequest.getPassword()
                )
        );
        return jwtTokenManager.generateToken(signInRequest.getLogin());
    }

    public User getCurrentAuthenticatedUserOrThrow() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Authentication not present");
        }
        return (User) authentication.getPrincipal();
    }
}