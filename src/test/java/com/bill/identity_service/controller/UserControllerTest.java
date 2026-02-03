/* (C)2025 */
package com.bill.identity_service.controller;

import com.bill.identity_service.dto.request.UserCreationRequest;
import com.bill.identity_service.dto.response.UserResponse;
import com.bill.identity_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private UserService userService;

    private UserCreationRequest userCreationRequest;

    private UserResponse userResponse;

    @BeforeEach
    void initData() {
        LocalDate dob = LocalDate.of(1997, 5, 20);
        userCreationRequest =
                userCreationRequest
                        .builder()
                        .username("john123")
                        .firstname("john")
                        .lastname("son")
                        .password("12345678")
                        .dob(dob)
                        .build();
        userResponse =
                UserResponse.builder()
                        .id("qwuahdasdhas")
                        .dob(dob)
                        .firstname("john")
                        .lastname("son")
                        .username("bill123")
                        .build();
        ;
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);

        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);
        // WHEN, THEN
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect((MockMvcResultMatchers.jsonPath("result.id").value("qwuahdasdhas")));
    }

    @Test
    void createUser_passwordInvalid_fail() throws Exception {
        // GIVEN
        userCreationRequest.setPassword("123");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);

        // WHEN, THEN
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(104))
                .andExpect(
                        (MockMvcResultMatchers.jsonPath("message")
                                .value("Password must be at least 8 characters!")));
    }
}
