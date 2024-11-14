package ru.pas_zhukov.eventmanager.util;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.pas_zhukov.eventmanager.converter.UserConverter;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.model.UserRole;
import ru.pas_zhukov.eventmanager.repository.UserRepository;

@Component
public class DefaultUserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;

    public DefaultUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userConverter = userConverter;
    }

    @PostConstruct
    public void initUsers() {
        createUserIfNotExists("admin", "admin", UserRole.ADMIN);
        createUserIfNotExists("user", "user", UserRole.USER);
    }

    private void createUserIfNotExists(String login, String password, UserRole role) {
        if (userRepository.existsByLogin(login)) {
            return;
        }
        String hashedPass = passwordEncoder.encode(password);
        User user = new User(
                null,
                login,
                hashedPass,
                21,
                role
        );
        userRepository.save(userConverter.toEntity(user));
    }
}
