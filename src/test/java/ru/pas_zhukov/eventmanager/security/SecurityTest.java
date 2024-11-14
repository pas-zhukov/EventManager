package ru.pas_zhukov.eventmanager.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import ru.pas_zhukov.eventmanager.TestInContainer;
import ru.pas_zhukov.eventmanager.converter.LocationConverter;
import ru.pas_zhukov.eventmanager.dto.request.LocationRequestDto;
import ru.pas_zhukov.eventmanager.dto.request.SignInRequestDto;
import ru.pas_zhukov.eventmanager.dto.request.SignUpRequestDto;
import ru.pas_zhukov.eventmanager.dto.response.JwtResponseDto;
import ru.pas_zhukov.eventmanager.exception.ServerErrorDto;
import ru.pas_zhukov.eventmanager.model.Location;
import ru.pas_zhukov.eventmanager.service.LocationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityTest extends TestInContainer {

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationConverter locationConverter;

    @Test
    public void shouldReturnUnauthorizedOnApiUsageWithoutAuth() throws Exception {
        LocationRequestDto locationRequestDto = new LocationRequestDto(null,
                "Hermitage",
                "Saint-Petersburg City, Dvortsovaya ploschad, 1",
                1000,
                null);
        Location createdLocation = locationService.createLocation(locationConverter.toDomain(locationRequestDto));

        String gotLocationJson = mockMvc.perform(get("/locations/{id}", createdLocation.getId()))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ServerErrorDto errorMessage = jacksonObjectMapper.readValue(gotLocationJson, ServerErrorDto.class);
        Assertions.assertEquals("Failed to authenticate", errorMessage.message());
        Assertions.assertEquals("Full authentication is required to access this resource", errorMessage.detailedMessage());
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"USER"})
    public void shouldReturnForbiddenOnLocationCreationAsUser() throws Exception {
        LocationRequestDto locationRequestDto = new LocationRequestDto(null,
                "Hermitage",
                "Saint-Petersburg City, Dvortsovaya ploschad, 1",
                1000,
                "some description");
        String locationJson = jacksonObjectMapper.writeValueAsString(locationRequestDto);
        String responseJson = mockMvc.perform(post("/locations").contentType(MediaType.APPLICATION_JSON).content(locationJson))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ServerErrorDto serverErrorDto = jacksonObjectMapper.readValue(responseJson, ServerErrorDto.class);
        Assertions.assertEquals("Forbidden", serverErrorDto.message());
        Assertions.assertEquals("Access Denied", serverErrorDto.detailedMessage());
    }

    @Test
    public void shouldSuccessSignUpSignIn() throws Exception {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("pasha", "password", 25);
        String signUpJson = jacksonObjectMapper.writeValueAsString(signUpRequestDto);
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(signUpJson))
                .andExpect(status().isCreated());

        SignInRequestDto signInRequestDto = new SignInRequestDto("pasha", "password");
        String signInJson = jacksonObjectMapper.writeValueAsString(signInRequestDto);
        String jwtString = mockMvc.perform(post("/users/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signInJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JwtResponseDto jwtResponseDto = jacksonObjectMapper.readValue(jwtString, JwtResponseDto.class);
        Assertions.assertNotNull(jwtResponseDto.jwtToken());
    }
}
