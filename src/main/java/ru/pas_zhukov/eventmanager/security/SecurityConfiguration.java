package ru.pas_zhukov.eventmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import ru.pas_zhukov.eventmanager.exception.CustomAccessDeniedHandler;
import ru.pas_zhukov.eventmanager.exception.CustomAuthenticationEntryPoint;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, "/locations")
                        .hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/locations/**")
                        .hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/locations")
                        .hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/locations/**")
                        .hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/locations/**")
                        .hasAnyAuthority("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/users")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/auth")
                        .permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler))
                .addFilterBefore(jwtTokenFilter, AnonymousAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
