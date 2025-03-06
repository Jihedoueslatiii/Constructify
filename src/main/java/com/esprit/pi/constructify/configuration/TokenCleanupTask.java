package com.esprit.pi.constructify.configuration;

import com.esprit.pi.constructify.repositories.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Component
public class TokenCleanupTask {

    @Autowired
    private UserRepository userRepository;

    // Exécute la tâche toutes les minutes (ajustez selon vos besoins)
    @Scheduled(cron = "0 * * * * ?") // Toutes les minutes
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        userRepository.deleteByExpiryDateBefore(now); // Supprime les utilisateurs expirés
        System.out.println("Expired users cleanup executed at: " + now);
    }
}
