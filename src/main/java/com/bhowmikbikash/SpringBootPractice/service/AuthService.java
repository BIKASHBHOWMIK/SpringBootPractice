package com.bhowmikbikash.SpringBootPractice.service;

import com.bhowmikbikash.SpringBootPractice.entity.User;
import com.bhowmikbikash.SpringBootPractice.exception.UserAuthenticationException;
import com.bhowmikbikash.SpringBootPractice.repository.UserRepository;
import com.bhowmikbikash.SpringBootPractice.util.JwtUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String signup(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("USER");
            }
            userRepository.save(user);
            return "User saved Successfully";
        } catch (Exception e) {
            throw new UserAuthenticationException("User Signup failed", e);
        }
    }

    public String login(String username, String password) {
        try {
            User dbUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (passwordEncoder.matches(password, dbUser.getPassword())) {
                return jwtUtil.generateToken(username);
            } else {
                throw new UserAuthenticationException("Invalid username or password");
            }
        } catch (UsernameNotFoundException e) {
            throw new UserAuthenticationException("Invalid username or password", e);
        }
    }
}
