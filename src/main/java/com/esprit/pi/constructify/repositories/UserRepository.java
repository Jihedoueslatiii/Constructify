package com.esprit.pi.constructify.repositories;

import com.esprit.pi.constructify.entities.User;
import com.esprit.pi.constructify.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(Role role);
    Optional<User> findByEmail(String email);  // Assure-toi que ton champ email existe bien dans l'entité User

    boolean existsByEmail(String email);

}
