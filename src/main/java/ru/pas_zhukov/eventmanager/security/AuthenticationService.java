package ru.pas_zhukov.eventmanager.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.pas_zhukov.eventmanager.dto.request.SignInRequestDto;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.service.UserService;

@Service
public class AuthenticationService {

    public static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            JwtTokenManager jwtTokenManager,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
        this.userService = userService;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Authentication not present");
        }
        logger.error(authentication.getPrincipal().getClass().getName());
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        } else if (principal instanceof org.springframework.security.core.userdetails.User) {
            return userService.getUserByLogin(
                    ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername()
            );
        } else {
            throw new IllegalStateException("Principal is not a user");
        }
    }
}