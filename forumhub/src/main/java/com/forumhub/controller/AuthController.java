package com.forumhub.controller;

import com.forumhub.dto.AuthRequest;
import com.forumhub.dto.RegisterDTO;
import com.forumhub.entity.User;
import com.forumhub.repository.UserRepository;
import com.forumhub.service.JwtService;
import com.forumhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;


    public AuthController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        System.out.println(userRepository.findAll());
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
        try {
            userService.registerUser(dto);
            return ResponseEntity.ok("Usuário registrado com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody AuthRequest request) {
            User user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

            if (!passwordEncoder.matches(request.password(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
            }
            System.out.println("Usuario autenticado com sucesso");
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(token);
        }
}
