package ru.pas_zhukov.eventmanager.converter;

import org.springframework.stereotype.Component;
import ru.pas_zhukov.eventmanager.dto.request.SignUpRequestDto;
import ru.pas_zhukov.eventmanager.dto.response.UserResponseDto;
import ru.pas_zhukov.eventmanager.entity.UserEntity;
import ru.pas_zhukov.eventmanager.model.User;

@Component
public class UserConverter {

    public UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getLogin(), user.getAge(), user.getRole());
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(user.getId(), user.getLogin(), user.getPasswordHash(), user.getAge(), user.getRole());
    }

    public User toDomain(UserEntity userEntity) {
        return new User(userEntity.getId(), userEntity.getLogin(), userEntity.getPasswordHash(), userEntity.getAge(), userEntity.getRole());
    }

    public User toDomain(SignUpRequestDto signUpRequestDto) {
        return new User(null, signUpRequestDto.getLogin(), signUpRequestDto.getPassword(), signUpRequestDto.getAge(), null);
    }
}
