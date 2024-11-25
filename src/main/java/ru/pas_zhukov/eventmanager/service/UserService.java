package ru.pas_zhukov.eventmanager.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.pas_zhukov.eventmanager.converter.UserConverter;
import ru.pas_zhukov.eventmanager.dto.request.SignUpRequestDto;
import ru.pas_zhukov.eventmanager.entity.UserEntity;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.model.UserRole;
import ru.pas_zhukov.eventmanager.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserConverter userConverter, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByLogin(signUpRequestDto.getLogin())) {
            throw new DuplicateKeyException("Login already taken");
        }
        User user = userConverter.toDomain(signUpRequestDto);
        user.setPasswordHash(passwordEncoder.encode(signUpRequestDto.getPassword()));
        user.setRole(UserRole.USER);
        UserEntity createdUser = userRepository.save(userConverter.toEntity(user));
        return userConverter.toDomain(createdUser);
    }

    public User getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id %s not found".formatted(id)));
        return userConverter.toDomain(userEntity);
    }

    public User getUserByLogin(String login) {
        UserEntity foundUser = userRepository.findByLogin(login).orElseThrow(() -> new EntityNotFoundException("User with login %s not found".formatted(login)));
        return userConverter.toDomain(foundUser);
    }

}
