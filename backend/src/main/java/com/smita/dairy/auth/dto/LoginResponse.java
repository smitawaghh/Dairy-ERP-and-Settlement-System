package com.smita.dairy.auth.dto;

public class LoginResponse {

    private String token;
    private String tokenType;
    private String username;
    private String role;

    public LoginResponse(
            String token,
            String tokenType,
            String username,
            String role) {

        this.token = token;
        this.tokenType = tokenType;
        this.username = username;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}