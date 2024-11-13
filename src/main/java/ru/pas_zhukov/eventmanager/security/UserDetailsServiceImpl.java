package ru.pas_zhukov.eventmanager.security;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.pas_zhukov.eventmanager.entity.UserEntity;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.repository.UserRepository;
import ru.pas_zhukov.eventmanager.service.UserService;

/**
 * Сервис для использования в качестве {@link UserDetailsService}
 * в {@code AuthenticationProvider}
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Получает юзера из БД по логину,
     * затем возвращает {@code UserDetails}
     * с полными данными пользователя (логин, хеш пароля, роль)
     * @param username логин юзера
     * @return {@link UserDetails} с данными юзера
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByLogin(username).orElseThrow(() ->
                new EntityNotFoundException("User with login %s not found".formatted(username)));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getLogin())
                .password(user.getPasswordHash())
                .authorities(user.getRole().name())
                .build();
    }
}
