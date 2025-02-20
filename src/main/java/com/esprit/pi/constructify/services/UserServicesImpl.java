package com.esprit.pi.constructify.services;

import com.esprit.pi.constructify.entities.Role;
import com.esprit.pi.constructify.entities.User;
import com.esprit.pi.constructify.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServicesImpl implements IUserService {

    private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public void UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword()); // Hacher le mot de passe
        user.setPassword(hashedPassword); // Remplacer le mot de passe en clair par le mot de passe haché
        return userRepository.save(user);
    }
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public void deleteUser(Long userId) {
        // Vérifier si l'utilisateur existe avant de tenter de le supprimer
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId);
        }
    }
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null); // Récupérer l'utilisateur par son ID
    }


}
