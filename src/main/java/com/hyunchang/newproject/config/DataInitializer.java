package com.hyunchang.newproject.config;

import com.hyunchang.newproject.service.AuthService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final AuthService authService;

    public DataInitializer(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void run(ApplicationArguments args) {
        authService.seedDefaultUserIfEmpty();
    }
}
