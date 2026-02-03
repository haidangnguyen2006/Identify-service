/* (C)2025 */
package com.bill.identity_service.configuration;

import com.bill.identity_service.dto.request.IntrospectRequest;
import com.bill.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class CustomJwtDecode implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String secretKey;

    @Autowired private AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            var isvalid =
                    authenticationService
                            .introspect(IntrospectRequest.builder().token(token).build())
                            .isValid();
            if (!isvalid) {
                throw new JwtException("Invalid token");
            }
        } catch (JOSEException | ParseException e) {
            e.printStackTrace();
        }
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HS512");
            nimbusJwtDecoder =
                    NimbusJwtDecoder.withSecretKey(secretKeySpec)
                            .macAlgorithm(MacAlgorithm.HS512)
                            .build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}
