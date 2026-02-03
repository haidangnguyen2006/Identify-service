/* (C)2025 */
package com.bill.identity_service.controller;

import com.bill.identity_service.dto.request.*;
import com.bill.identity_service.dto.response.AuthenticationResponse;
import com.bill.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    /*
    Authenticate user and generate token
    @param request AuthenticationRequest
    @return AuthenticationResponse
     */
    @PostMapping("/token")
    ApiRespone<AuthenticationResponse> authenticated(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse result = authenticationService.authenticated(request);
        return ApiRespone.<AuthenticationResponse>builder().result(result).build();
    }

    /*
    Introspect token to check valid or not
    @param request IntrospectRequest
    @return IntrospectRespone
     */
    @PostMapping("/logout")
    ApiRespone<Void> authenticated(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiRespone.<Void>builder().build();
    }

    @PostMapping("/refresh")
    ApiRespone<AuthenticationResponse> authenticated(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        AuthenticationResponse result = authenticationService.refreshToken(request);
        return ApiRespone.<AuthenticationResponse>builder().result(result).build();
    }
}
