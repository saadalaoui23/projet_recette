package com.example.user_service.services;

import com.example.user_service.dtos.RegisterRequest;
import com.example.user_service.models.User;
import com.example.user_service.repositories.UserRepository;
import com.example.user_service.security.JwtProvider; // 👈 NOUVEL IMPORT
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider; // 👈 INJECTION DU JWT PROVIDER

    // --- Logique pour l'API Feign (Validation d'existence) ---
    @Override
    public boolean userExists(Long userId) {
        return userRepository.existsById(userId);
    }

    // --- Logique d'Enregistrement (Reste inchangée) ---
    @Override
    public User register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("L'email est déjà utilisé.");
        }

        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        newUser.setPassword(hashedPassword);

        newUser.setProvider("LOCAL");
        newUser.setRole("ROLE_USER");

        return userRepository.save(newUser);
    }

    // --- Logique de Connexion (Modifiée pour retourner le JWT) ---
    @Override
    public String login(String email, String rawPassword) { // 👈 CHANGEMENT DE TYPE DE RETOUR (String)
        // 1. Chercher l'utilisateur par email
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("Identifiants invalides : Utilisateur non trouvé.");
        }

        // 2. Vérifier si le mot de passe hashé correspond au mot de passe brut fourni
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Identifiants invalides : Mot de passe incorrect.");
        }

        // 3. Connexion réussie : Générer le JWT
        return jwtProvider.generateToken(user.getEmail(), user.getRole());
    }
}