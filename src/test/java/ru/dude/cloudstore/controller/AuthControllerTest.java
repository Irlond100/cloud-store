package ru.dude.cloudstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.dude.cloudstore.dto.AuthRequest;
import ru.dude.cloudstore.dto.HeaderNameHolder;
import ru.dude.cloudstore.model.TokenResponse;
import ru.dude.cloudstore.service.AuthWithJWTService;


import static ru.dude.cloudstore.controller.TestConstantHolder.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {
    private static AuthWithJWTService mockAuthWithJWTService;
    private static MockMvc mockMvc;
    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setup() {
        mockAuthWithJWTService = Mockito.mock(AuthWithJWTService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(mockAuthWithJWTService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void loginSuccess() throws Exception {
        final var authRequest = new AuthRequest(TEST_USERNAME, TEST_PASSWORD);
        final var tokenResponse = new TokenResponse(TEST_JWT);
        when(mockAuthWithJWTService.login(any(AuthRequest.class))).thenReturn(tokenResponse);
        final var resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(HeaderNameHolder.TOKEN_HEADER_NAME).value(TEST_JWT));

        verify(mockAuthWithJWTService).login(authRequest);
    }

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

}
