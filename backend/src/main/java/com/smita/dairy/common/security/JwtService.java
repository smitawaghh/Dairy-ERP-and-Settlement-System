package com.smita.dairy.common.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.expiration-minutes:120}")
    private long expirationMinutes;

    public JwtService(
            JwtEncoder jwtEncoder) {

        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(
            Authentication authentication) {

        Instant now = Instant.now();

        String roles =
                authentication
                        .getAuthorities()
                        .stream()
                        .map(authority ->
                                authority.getAuthority()
                        )
                        .collect(
                                Collectors.joining(" ")
                        );

        JwtClaimsSet claims =
                JwtClaimsSet.builder()
                        .issuer("dairy-management-system")
                        .issuedAt(now)
                        .expiresAt(
                                now.plus(
                                        expirationMinutes,
                                        ChronoUnit.MINUTES
                                )
                        )
                        .subject(
                                authentication.getName()
                        )
                        .claim("roles", roles)
                        .build();

        JwsHeader headers =
                JwsHeader
                        .with(MacAlgorithm.HS256)
                        .build();

        return jwtEncoder
                .encode(
                        JwtEncoderParameters.from(
                                headers,
                                claims
                        )
                )
                .getTokenValue();
    }
}