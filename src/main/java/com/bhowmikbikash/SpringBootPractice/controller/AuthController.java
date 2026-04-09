package com.bhowmikbikash.SpringBootPractice.controller;

import com.bhowmikbikash.SpringBootPractice.entity.User;
import com.bhowmikbikash.SpringBootPractice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            String message = authService.signup(user);
            return ResponseEntity.ok().body(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            String token = authService.login(user.getUsername(), user.getPassword());
            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
