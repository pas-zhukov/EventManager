package ru.pas_zhukov.eventmanager.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import ru.pas_zhukov.eventmanager.TestInContainer;
import ru.pas_zhukov.eventmanager.converter.UserConverter;
import ru.pas_zhukov.eventmanager.dto.request.SignUpRequestDto;
import ru.pas_zhukov.eventmanager.dto.response.UserResponseDto;
import ru.pas_zhukov.eventmanager.exception.ServerErrorDto;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.model.UserRole;
import ru.pas_zhukov.eventmanager.service.UserService;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends TestInContainer {

    @Autowired
    private UserService userService;
    @Autowired
    private UserConverter userConverter;

    @Test
    public void shouldSuccessOnRegisterUser() throws Exception {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("pashok", "password", 25);
        String signUpJson = jacksonObjectMapper.writeValueAsString(signUpRequestDto);
        String responseJson = mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(signUpJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        UserResponseDto createdUser = jacksonObjectMapper.readValue(responseJson, UserResponseDto.class);

        Assertions.assertNotNull(createdUser);
        Assertions.assertNotNull(createdUser.getId());
        Assertions.assertEquals(signUpRequestDto.getLogin(), createdUser.getLogin());
        Assertions.assertEquals(signUpRequestDto.getAge(), createdUser.getAge());
        Assertions.assertEquals(UserRole.USER, createdUser.getRole());
    }

    @Test
    @WithMockUser(username = "testadmin", authorities = {"ADMIN"})
    public void shouldSuccessOnGetUserById() throws Exception {
        User createdUser = userService.registerUser(new SignUpRequestDto("pashka", "password", 25));
        String responseJson = mockMvc.perform(get("/users/{id}", createdUser.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        UserResponseDto gotUser = jacksonObjectMapper.readValue(responseJson, UserResponseDto.class);

        org.assertj.core.api.Assertions.assertThat(userConverter.toResponseDto(createdUser)).usingRecursiveComparison().isEqualTo(gotUser);
    }

    @Test
    public void shouldNotCreateUserOnInvalidRequest() throws Exception {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("pas", "", null);
        String signUpJson = jacksonObjectMapper.writeValueAsString(signUpRequestDto);
        String responseJson = mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(signUpJson))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ServerErrorDto serverErrorDto = jacksonObjectMapper.readValue(responseJson, ServerErrorDto.class);
        Assertions.assertEquals("Validation error", serverErrorDto.message());
    }

    @Test
    public void shouldNotCreateUserWithDuplicateLogin() throws Exception {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("pashok", "password", 25);
        String signUpJson = jacksonObjectMapper.writeValueAsString(signUpRequestDto);
        String responseJson = mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(signUpJson))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(responseJson);
        ServerErrorDto serverErrorDto = jacksonObjectMapper.readValue(responseJson, ServerErrorDto.class);
        Assertions.assertEquals("Validation error", serverErrorDto.message());
    }

    @Test
    @WithMockUser(username = "testadmin", authorities = {"ADMIN"})
    public void shouldReturnNotFoundOnGetNonExistingUserById() throws Exception {
        mockMvc.perform(get("/users/{id}", Long.MAX_VALUE))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
}
