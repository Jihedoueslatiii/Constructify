package com.esprit.pi.constructify.repositories;

import com.esprit.pi.constructify.entities.User;
import com.esprit.pi.constructify.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(Role role);
    Optional<User> findByEmail(String email);
    Page<User> findByRole(String role, Pageable pageable);// Assure-toi que ton champ email existe bien dans l'entité User
    List<User> findByTeamId(Long teamId);
    Optional<User> findByConfirmationToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.expiryDate < :now")
    void deleteByExpiryDateBefore(LocalDateTime now);
}
