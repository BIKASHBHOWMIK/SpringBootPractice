package com.bhowmikbikash.SpringBootPractice.controller;

import com.bhowmikbikash.SpringBootPractice.entity.User;
import com.bhowmikbikash.SpringBootPractice.repository.UserRepository;
import com.bhowmikbikash.SpringBootPractice.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("USER");
            userRepository.save(user);
            return ResponseEntity.ok().body("User saved Successfully");
        } catch (Exception e) {
            throw new RuntimeException("User Signup failed");
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        try {
            User dbUser = userRepository.findByUsername(user.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));
            if(passwordEncoder.matches(user.getPassword(),dbUser.getPassword())){
                return ResponseEntity.ok().body(jwtUtil.generateToken(user.getUsername()));
            }
            throw new RuntimeException("Invalid username or password");
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
