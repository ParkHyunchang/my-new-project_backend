package com.hyunchang.newproject.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AdminSessionService {

    private final Map<String, String> tokenStore = new ConcurrentHashMap<>();

    public String createToken(String username) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, username);
        return token;
    }

    public boolean isValidToken(String token) {
        return token != null && tokenStore.containsKey(token);
    }

    public void removeToken(String token) {
        if (token != null) {
            tokenStore.remove(token);
        }
    }
}
