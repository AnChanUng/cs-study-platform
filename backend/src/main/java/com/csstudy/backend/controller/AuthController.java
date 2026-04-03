package com.csstudy.backend.controller;

import com.csstudy.backend.dto.UserResponse;
import com.csstudy.backend.entity.User;
import com.csstudy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        final String nickname = body.getOrDefault("nickname", "").trim();
        final String password = body.getOrDefault("password", "").trim();

        if (nickname.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "닉네임과 비밀번호를 입력해주세요."));
        }

        if (userRepository.existsByNickname(nickname)) {
            return ResponseEntity.badRequest().body(Map.of("error", "이미 사용 중인 닉네임입니다."));
        }

        User user = userRepository.save(
                User.builder().nickname(nickname).password(password).build()
        );
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        final String nickname = body.getOrDefault("nickname", "").trim();
        final String password = body.getOrDefault("password", "").trim();

        if (nickname.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "닉네임과 비밀번호를 입력해주세요."));
        }

        Optional<User> userOpt = userRepository.findByNickname(nickname);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "존재하지 않는 닉네임입니다."));
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.badRequest().body(Map.of("error", "비밀번호가 틀렸습니다."));
        }

        return ResponseEntity.ok(UserResponse.from(user));
    }
}
