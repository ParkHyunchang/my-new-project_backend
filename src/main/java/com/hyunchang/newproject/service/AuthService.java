package com.hyunchang.newproject.service;

import com.hyunchang.newproject.dto.LoginResponse;
import com.hyunchang.newproject.entity.User;
import com.hyunchang.newproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String seedDefaultUsername;
    private final String seedDefaultPassword;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       @Value("${seed.default-username:}") String seedDefaultUsername,
                       @Value("${seed.default-password:}") String seedDefaultPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.seedDefaultUsername = seedDefaultUsername != null ? seedDefaultUsername : "";
        this.seedDefaultPassword = seedDefaultPassword != null ? seedDefaultPassword : "";
    }

    public LoginResponse login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return LoginResponse.fail("아이디와 비밀번호를 입력해주세요.");
        }

        Optional<User> optUser = userRepository.findByUsername(username.trim());
        if (optUser.isEmpty()) {
            return LoginResponse.fail("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        User user = optUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return LoginResponse.fail("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return LoginResponse.success(user.getUsername());
    }

    public void seedDefaultUserIfEmpty() {
        if (seedDefaultUsername.isBlank() || seedDefaultPassword.isBlank()) {
            return;
        }
        if (userRepository.existsByUsername(seedDefaultUsername)) {
            return;
        }
        String hashedPassword = passwordEncoder.encode(seedDefaultPassword);
        userRepository.save(new User(seedDefaultUsername, hashedPassword));
    }
}
