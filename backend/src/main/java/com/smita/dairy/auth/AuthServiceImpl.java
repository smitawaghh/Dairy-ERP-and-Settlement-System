package com.smita.dairy.auth;

import com.smita.dairy.auth.dto.LoginRequest;
import com.smita.dairy.auth.dto.LoginResponse;
import com.smita.dairy.auth.exception.InvalidCredentialsException;

import com.smita.dairy.common.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl
        implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository) {

        this.authenticationManager =
                authenticationManager;

        this.jwtService =
                jwtService;

        this.userRepository =
                userRepository;
    }

    @Override
    public LoginResponse login(
            LoginRequest request) {

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(),
                                    request.getPassword()
                            )
                    );

            AppUser user =
                    userRepository
                            .findByUsername(
                                    request.getUsername()
                            )
                            .orElseThrow(() ->
                                    new InvalidCredentialsException(
                                            "Invalid username or password"
                                    )
                            );

            String token =
                    jwtService.generateToken(
                            authentication
                    );

            return new LoginResponse(
                    token,
                    "Bearer",
                    user.getUsername(),
                    user.getRole().name()
            );

        } catch (BadCredentialsException exception) {

            throw new InvalidCredentialsException(
                    "Invalid username or password"
            );
        }
    }
}