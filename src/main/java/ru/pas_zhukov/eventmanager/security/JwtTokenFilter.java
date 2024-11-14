package ru.pas_zhukov.eventmanager.security;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.pas_zhukov.eventmanager.converter.UserConverter;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.repository.UserRepository;

import java.io.IOException;
import java.util.List;

/**
 * Фильтр для применения в {@code SecurityFilterChain}.
 * Проверяет наличие корректного JWT-токена в заголовке запроса
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenManager jwtTokenManager;
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public JwtTokenFilter(JwtTokenManager jwtTokenManager, UserRepository userRepository, UserConverter userConverter) {
        this.jwtTokenManager = jwtTokenManager;
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    /**
     * Порядок работы фильтра:
     *  <p>1. Проверка на наличие заголовка авторизации, и что он начинается с "Bearer "</p>
     *  <p>2. Расшифровка токена и получение из него логина</p>
     *  <p>3. Получение пользователя по логину и аутентификация в случае, если пользователь найден.</p>
     *  <p>4. Сохранение аутентификации пользователя в контексте секьюрити.</p>
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        var jwtToken = authorizationHeader.substring(7);
        String loginFromToken;
        try {
            loginFromToken = jwtTokenManager.getLoginFromToken(jwtToken);
        } catch (Exception e) {
            logger.error("Error while reading JWT", e);
            filterChain.doFilter(request, response);
            return;
        }
        User user = userConverter.toDomain(userRepository.findByLogin(loginFromToken).orElseThrow(() ->
                new EntityNotFoundException("User with login %s not found".formatted(loginFromToken))));
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user,
                null,
                List.of(new SimpleGrantedAuthority(user.getRole().toString()))
        );
        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request, response);
    }
}
