package ru.pas_zhukov.eventmanager.service;

import jakarta.persistence.EntityNotFoundException;
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

    public UserService(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    public User registerUser(SignUpRequestDto signUpRequestDto) {
        //
        User user = new User();
        user.setLogin(signUpRequestDto.getLogin());
        user.setPasswordHash(signUpRequestDto.getPassword());
        user.setAge(signUpRequestDto.getAge());
        user.setRole(UserRole.USER);
        UserEntity createdUser = userRepository.save(userConverter.toEntity(user));
        //
        return userConverter.toDomain(createdUser);
    }

    public User getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id %s not found".formatted(id)));
        return userConverter.toDomain(userEntity);
    }
}
