package com.hyunchang.newproject.controller;

import com.hyunchang.newproject.dto.LoginRequest;
import com.hyunchang.newproject.dto.LoginResponse;
import com.hyunchang.newproject.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        LoginResponse response = authService.login(
                request.getUsername(),
                request.getPassword()
        );
        if (response.isSuccess()) {
            log.info("[로그인 성공] user={}, ip={}", request.getUsername(), ip);
            return ResponseEntity.ok(response);
        }
        log.warn("[로그인 실패] user={}, ip={}", request.getUsername(), ip);
        return ResponseEntity.status(401).body(response);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) return ip.split(",")[0].trim();
        return request.getRemoteAddr();
    }
}
