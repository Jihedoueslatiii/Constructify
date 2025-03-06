package com.esprit.pi.constructify.controllers;

import com.esprit.pi.constructify.entities.GoogleUserDTO;
import com.esprit.pi.constructify.entities.Role;
import com.esprit.pi.constructify.entities.User;
import com.esprit.pi.constructify.repositories.UserRepository;
import com.esprit.pi.constructify.services.IUserService;
import com.esprit.pi.constructify.services.JwtService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        // Générer un token de confirmation
        String confirmationToken = UUID.randomUUID().toString(); // Génère un token unique
        user.setConfirmationToken(confirmationToken);

        // Définir une date d'expiration (par exemple, 24 heures)
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(24); // Ajoute 24 heures au temps actuel
        user.setExpiryDate(expiryDate);

        // Sauvegarder l'utilisateur
        User savedUser = userService.saveUser(user);

        // Envoyer un e-mail de confirmation
        sendConfirmationEmail(savedUser.getEmail(), savedUser.getConfirmationToken());

        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<?> confirmAccount(@RequestParam("token") String token) {
        try {
            // Trouver l'utilisateur par le token de confirmation
            User user = userService.findByConfirmationToken(token);

            if (user == null) {
                return ResponseEntity.badRequest().body("\"Your account has been deleted due to inactivity. \" +\n" +
                        "                                \"The confirmation token has expired, and you did not confirm your account within the required time. \" +\n" +
                        "                                \"Please register again to create a new account.\"");
            }

            // Vérifier si le token a expiré
            if (user.getExpiryDate().isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body(
                        "Your account has been deleted due to inactivity. " +
                                "The confirmation token has expired, and you did not confirm your account within the required time. " +
                                "Please register again to create a new account."
                );
            }

            // Activer le compte
            user.setStatus("Activated");
            user.setConfirmationToken(null); // Supprimer le token après confirmation
            user.setExpiryDate(null); // Supprimer la date d'expiration

            // Sauvegarder les modifications
            userService.saveUser2(user);

            return ResponseEntity.ok("Account confirmed successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    private void sendConfirmationEmail(String toEmail, String confirmationToken) {
        String confirmationUrl = "http://localhost:8089/Constructify/user/confirm-account?token=" + confirmationToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Activate Your Constructify Account");
        message.setText(
                "Hello,\n\n" +
                        "Welcome to Constructify! Please confirm your email address by clicking the link below:\n\n" +
                        confirmationUrl + "\n\n" +
                        "This link will expire in 24 hours. If you did not create an account, you can safely ignore this email.\n\n" +
                        "Thank you,\n" +
                        "Constructify Support"
        );

        mailSender.send(message);
    }


    @GetMapping("/all")
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<User> users = userService.getUsers(page, size);
        return ResponseEntity.ok(users);
    }


    @GetMapping("/getUserByRole/{role}")
    public List<User> getUsersByRole(@PathVariable Role role) {
        return userService.getUsersByRole(role);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Utilisateur supprimé avec succès"); // Renvoie un objet JSON
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Utilisateur non trouvé"); // Renvoie un objet JSON en cas d'erreur
            return ResponseEntity.status(404).body(response);
        }
    }

    @PutMapping("/UpdateUserRole/{id}")
    public ResponseEntity<User> updateUserRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) {

        // Extraire le rôle depuis la requête JSON
        String roleString = requestBody.get("role");

        // Vérifier si la valeur est correcte
        Role newRole;
        try {
            newRole = Role.valueOf(roleString);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Vérifier si l'utilisateur existe
        Optional<User> optionalUser = userService.findUserById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Mettre à jour le rôle de l'utilisateur
        User user = optionalUser.get();
        user.setRole(newRole);
        User updatedUser = userService.saveUser2(user);

        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/GetuserById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }


    @GetMapping("/byTeam/{teamId}")
    public ResponseEntity<List<User>> getUsersByTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(userService.getUsersByTeam(teamId));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required."));
            }

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Invalid email."));

            if (!"Activated".equals(user.getStatus())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Your account is not activated. Please confirm your email."));
            }

            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid password."));
            }

            // Générer un JWT
            String token = jwtService.generateToken(user);

            // Retourner le JWT et les détails de l'utilisateur
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "token", token,
                    "redirectUrl", determineRedirectUrl(user.getRole()),
                    "role", user.getRole().name(),
                    "user", user
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }




    @GetMapping("/session")
    public ResponseEntity<?> getSessionInfo(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No active session.");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", session.getId());
        response.put("user", user);
        response.put("creationTime", session.getCreationTime());
        response.put("lastAccessedTime", session.getLastAccessedTime());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // Supprimer la session
        return ResponseEntity.ok("Logout successful.");
    }


    private String determineRedirectUrl(Role role) {
        switch (role) {
            case Admin:
                return "http://localhost:4200/user-list"; // Redirection pour les administrateurs
            case Client:
                return "http://localhost:4200/clientInterface";  // Redirection pour les utilisateurs normaux
            default:
                return "/"; // Redirection par défaut
        }
    }

    @PutMapping("/removeFromTeam/{userId}")
    public ResponseEntity<String> removeUserFromTeam(@PathVariable Long userId) {
        userService.removeUserFromTeam(userId);
        return ResponseEntity.ok("User removed from team successfully.");
    }

    @RequestMapping("/home")
    public String home(HttpSession session) {
        // Ajouter des informations dans la session si nécessaire
        session.setAttribute("username", "JohnDoe");

        // Ou récupérer une valeur de la session
        String username = (String) session.getAttribute("username");

        return "home";
    }

    @GetMapping("/mail")
    public String userInfo(@AuthenticationPrincipal OAuth2User principal, Model model) {
        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));
        return "user";
    }
    @PostMapping("/signup/google")
    public ResponseEntity<?> signUpWithGoogle(@RequestBody GoogleUserDTO googleUserDTO) {
        if (userService.existsByEmail(googleUserDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }

        // Crée l'utilisateur en base de données
        User newUser = new User();
        newUser.setFirstname(googleUserDTO.getFirstName());
        newUser.setLastname(googleUserDTO.getLastName());
        newUser.setEmail(googleUserDTO.getEmail());
        newUser.setPassword("");  // Si l'utilisateur se connecte via Google, il n'a pas de mot de passe pour l'instant
        newUser.setRole(Role.valueOf("Client"));  // ou un autre rôle que tu veux attribuer

        userService.saveUser2(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
}



