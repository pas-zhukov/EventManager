package ru.pas_zhukov.eventmanager.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pas_zhukov.eventmanager.TestInContainer;
import ru.pas_zhukov.eventmanager.dto.request.SignUpRequestDto;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.model.UserRole;

public class UserServiceTest extends TestInContainer {

    @Autowired
    private UserService userService;

    @Test
    public void successOnRegisterUser() {
        SignUpRequestDto userToCreate = new SignUpRequestDto(
                "user123",
                "password123",
                100);
        User createdUser = userService.registerUser(userToCreate);

        Assertions.assertNotNull(createdUser);
        Assertions.assertNotNull(createdUser.getId());
        Assertions.assertNotNull(createdUser.getRole());

        Assertions.assertEquals(userToCreate.getLogin(), createdUser.getLogin());
        Assertions.assertEquals(userToCreate.getAge(), createdUser.getAge());

        Assertions.assertNotEquals(userToCreate.getPassword(), createdUser.getPasswordHash());

        User userFromDB = userService.getUserByLogin(userToCreate.getLogin());

        Assertions.assertEquals(createdUser, userFromDB);
    }

    @Test
    public void checkDefaultUsersAreCreated() {
        User user = userService.getUserByLogin("user");
        User admin = userService.getUserByLogin("admin");

        Assertions.assertEquals(UserRole.USER, user.getRole());
        Assertions.assertEquals(UserRole.ADMIN, admin.getRole());
    }
}
