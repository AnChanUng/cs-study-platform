package com.csstudy.backend.controller;

import com.csstudy.backend.dto.UserResponse;
import com.csstudy.backend.entity.User;
import com.csstudy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String nickname = body.get("nickname");
        if (nickname == null || nickname.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "닉네임을 입력해주세요."));
        }
        nickname = nickname.trim();

        User user = userRepository.findByNickname(nickname)
                .orElseGet(() -> userRepository.save(
                        User.builder().nickname(nickname).build()
                ));

        return ResponseEntity.ok(UserResponse.from(user));
    }

    @GetMapping("/check/{nickname}")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@PathVariable String nickname) {
        boolean exists = userRepository.existsByNickname(nickname.trim());
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
