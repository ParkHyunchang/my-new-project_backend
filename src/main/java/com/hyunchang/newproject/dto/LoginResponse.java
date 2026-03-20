package com.hyunchang.newproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private boolean success;
    private String username;
    private String message;
    private String token;

    public static LoginResponse success(String username, String token) {
        return new LoginResponse(true, username, null, token);
    }

    public static LoginResponse fail(String message) {
        return new LoginResponse(false, null, message, null);
    }
}
