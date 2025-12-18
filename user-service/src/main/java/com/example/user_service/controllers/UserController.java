package com.example.user_service.controllers;

import com.example.user_service.dtos.AuthResponse;
import com.example.user_service.dtos.LoginRequest;
import com.example.user_service.dtos.RegisterRequest;
import com.example.user_service.models.User;
import com.example.user_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // --- 1. Endpoint pour l'API Feign (Validation d'existence) ---
    @GetMapping("/{userId}/exists")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable Long userId) {
        boolean exists = userService.userExists(userId);
        return ResponseEntity.ok(exists);
    }

    // --- 2. Endpoint d'Enregistrement (Reste inchangé) ---
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegisterRequest request) {
        try {
            User newUser = userService.register(request);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // --- 3. Endpoint de Connexion (Modifié pour retourner AuthResponse/JWT) ---
    // POST http://localhost:8083/api/users/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest request) {
        try {
            // Le service retourne directement le JWT
            String jwt = userService.login(request.getEmail(), request.getPassword());

            // Retourne le token dans l'objet AuthResponse
            return ResponseEntity.ok(new AuthResponse(jwt));
        } catch (RuntimeException e) {
            // Gère l'échec de l'authentification (401 Unauthorized)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}