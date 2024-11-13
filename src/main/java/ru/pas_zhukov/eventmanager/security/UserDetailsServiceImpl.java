package ru.pas_zhukov.eventmanager.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.service.UserService;

/**
 * Сервис для использования в качестве {@link UserDetailsService}
 * в {@code AuthenticationProvider}
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
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
        User user = userService.getUserByLogin(username);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getLogin())
                .password(user.getPasswordHash())
                .authorities(user.getRole().name())
                .build();
    }
}
