package com.forumhub.service;

import  com.forumhub.dto.RegisterDTO;
import com.forumhub.entity.User;
import com.forumhub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        User user = User.builder()
                        .name(dto.name())
                        .email(dto.email())
                        .password(passwordEncoder.encode(dto.password()))
                        .role("ROLE_USER")
                        .createdAt(LocalDate.from(LocalDateTime.now()))
                        .build();

        userRepository.save(user);
    }
}
