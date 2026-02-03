/* (C)2025 */
package com.bill.identity_service.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.bill.identity_service.dto.request.UserCreationRequest;
import com.bill.identity_service.dto.response.UserResponse;
import com.bill.identity_service.entity.User;
import com.bill.identity_service.exception.AppException;
import com.bill.identity_service.repository.UserRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.assertj.core.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired private UserService userService;

    @MockitoBean private UserRepository userRepository;

    private UserCreationRequest userCreationRequest;

    private UserResponse userResponse;

    private User user;

    @BeforeEach
    void initData() {
        LocalDate dob = LocalDate.of(1997, 5, 20);
        userCreationRequest =
                UserCreationRequest.builder()
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
        user =
                User.builder()
                        .username("john123")
                        .id("qwuahdasdhas")
                        .firstname("john")
                        .lastname("son")
                        .password("12345678")
                        .dob(dob)
                        .build();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(false);
        Mockito.when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var respone = userService.createUser(userCreationRequest);

        // THEN
        Assertions.assertThat(respone.getId()).isEqualTo("qwuahdasdhas");
        Assertions.assertThat(respone.getUsername()).isEqualTo("john123");
    }

    @Test
    void createUser_existUser_fail() {
        // GIVEN
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception =
                assertThrows(AppException.class, () -> userService.createUser(userCreationRequest));
        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(666);
    }

    @Test
    @WithMockUser(username = "john")
    void getMyinfo_valid_success() {
        // GIVEN
        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // WHEN
        var respone = userService.getMyInfo();

        // THEN
        Assertions.assertThat(respone.getId()).isEqualTo("qwuahdasdhas");
        Assertions.assertThat(respone.getUsername()).isEqualTo("john123");
    }

    @Test
    @WithMockUser(username = "john")
    void getMyinfo_nonexist_error() {
        // GIVEN
        Mockito.when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.ofNullable(null));

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());
        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(777);
    }
}
