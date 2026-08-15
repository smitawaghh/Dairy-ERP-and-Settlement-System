package com.smita.dairy.auth;

import com.smita.dairy.auth.dto.LoginRequest;
import com.smita.dairy.auth.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(
            LoginRequest request
    );
}