/* (C)2025 */
package com.bill.identity_service.service;

import com.bill.identity_service.dto.request.AuthenticationRequest;
import com.bill.identity_service.dto.request.IntrospectRequest;
import com.bill.identity_service.dto.request.LogoutRequest;
import com.bill.identity_service.dto.request.RefreshRequest;
import com.bill.identity_service.dto.response.AuthenticationResponse;
import com.bill.identity_service.dto.response.IntrospectRespone;
import com.bill.identity_service.entity.InvalidatedToken;
import com.bill.identity_service.entity.User;
import com.bill.identity_service.exception.AppException;
import com.bill.identity_service.exception.ErrorCode;
import com.bill.identity_service.mapper.UserMapper;
import com.bill.identity_service.repository.InvalidatedTokenRepository;
import com.bill.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNED_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    UserRepository userRepository;
    UserMapper userMapper;
    InvalidatedTokenRepository invalidatedTokenRepository;

    /*
    Introspect token to check valid or not
    @param request IntrospectRequest
    @return IntrospectRespone
     */
    public IntrospectRespone introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isvalid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isvalid = false;
        }
        return IntrospectRespone.builder().valid(isvalid).build();
    }

    /*
    Authenticate user and generate token
    @param request AuthenticationRequest
    @return AuthenticationResponse
     */
    public AuthenticationResponse authenticated(AuthenticationRequest request) {
        var user =
                userRepository
                        .findByUsername(request.getUsername())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_UN_EXISTED));
        PasswordEncoder password = new BCryptPasswordEncoder(10);
        boolean authenticate = password.matches(request.getPassword(), user.getPassword());
        if (!authenticate) {
            throw new AppException(ErrorCode.UNAUTHENTICATION);
        }
        var token = generateToken(user);
        return AuthenticationResponse.builder().authenticated(authenticate).token(token).build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        try {
            SignedJWT signedJWT = verifyToken(token, true);
            InvalidatedToken invalidatedToken =
                    new InvalidatedToken()
                            .builder()
                            .id(signedJWT.getJWTClaimsSet().getJWTID())
                            .exp(signedJWT.getJWTClaimsSet().getExpirationTime())
                            .build();
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException ex) {
            log.info("token already expirate");
        }
    }

    public AuthenticationResponse refreshToken(RefreshRequest request)
            throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken(), true);
        // Disable old token
        InvalidatedToken invalidatedToken =
                new InvalidatedToken()
                        .builder()
                        .id(signedJWT.getJWTClaimsSet().getJWTID())
                        .exp(signedJWT.getJWTClaimsSet().getExpirationTime())
                        .build();
        invalidatedTokenRepository.save(invalidatedToken);
        // Build refresh token
        var username = signedJWT.getJWTClaimsSet().getSubject();
        var user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATION));
        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh)
            throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNED_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        var verified = signedJWT.verify(jwsVerifier);

        Date exp =
                isRefresh
                        ? new Date(
                                signedJWT
                                        .getJWTClaimsSet()
                                        .getIssueTime()
                                        .toInstant()
                                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                                        .toEpochMilli())
                        : signedJWT.getJWTClaimsSet().getExpirationTime();

        if (!(verified && exp.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATION);
        }
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATION);
        }
        return signedJWT;
    }

    /*
    Generate JWT token
    @param user User
    @return String token
     */
    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet =
                new JWTClaimsSet.Builder()
                        .subject(user.getUsername())
                        .issuer("bill.com")
                        .issueTime(new Date())
                        .expirationTime(
                                new Date(
                                        Instant.now()
                                                .plus(VALID_DURATION, ChronoUnit.SECONDS)
                                                .toEpochMilli()))
                        .claim("scope", buildScope(user))
                        .jwtID(UUID.randomUUID().toString())
                        .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject token = new JWSObject(header, payload);
        try {
            token.sign(new MACSigner(SIGNED_KEY.getBytes()));
            return token.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new AppException(ErrorCode.CANNOT_CREATE_TOKEN);
        }
    }

    /*
    Build scope string from user roles
    @param user User
    @return String scope
     */
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles()
                    .forEach(
                            role -> {
                                stringJoiner.add("ROLE_" + role.getName());
                                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                                    role.getPermissions()
                                            .forEach(
                                                    permission -> {
                                                        stringJoiner.add(permission.getName());
                                                    });
                                }
                            });
        }
        return stringJoiner.toString();
    }
}
