package ru.pas_zhukov.eventmanager.converter;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import ru.pas_zhukov.eventmanager.dto.request.SignUpRequestDto;
import ru.pas_zhukov.eventmanager.dto.response.UserResponseDto;
import ru.pas_zhukov.eventmanager.entity.UserEntity;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.repository.UserRepository;

import java.util.ArrayList;

@Component
public class UserConverter {

    private final UserRepository userRepository;

    public UserConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getLogin(), user.getAge(), user.getRole());
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(user.getId(), user.getLogin(), user.getPasswordHash(), user.getAge(), user.getRole(), new ArrayList<>(), new ArrayList<>()); // TODO
    }

    public User toDomain(UserEntity userEntity) {
        return new User(userEntity.getId(), userEntity.getLogin(), userEntity.getPasswordHash(), userEntity.getAge(), userEntity.getRole());
    }

    public User toDomain(SignUpRequestDto signUpRequestDto) {
        return new User(null, signUpRequestDto.getLogin(), signUpRequestDto.getPassword(), signUpRequestDto.getAge(), null);
    }

    public User toDomain(org.springframework.security.core.userdetails.User user) {
        UserEntity foundUser = userRepository.findByLogin(user.getUsername()).orElseThrow(() -> new EntityNotFoundException("User with login %s not found".formatted(user.getUsername())));
        return toDomain(foundUser);
    }
}
