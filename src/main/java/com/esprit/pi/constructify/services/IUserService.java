package com.esprit.pi.constructify.services;

import com.esprit.pi.constructify.entities.Role;
import com.esprit.pi.constructify.entities.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User saveUser(User user);
    List<User> getAllUsers();
    List<User> getUsersByRole(Role role);
    void deleteUser(Long userId);

    User findByEmail(String email);

    boolean existsByEmail(String email);
    Optional<User> findUserById(Long id);
    User getUserById(Long id);
    List<User> getUsersByTeam(Long teamId);
    String generateConfirmationToken();
    User findByConfirmationToken(String token);

    User saveUser2(User user);

    void removeUserFromTeam(Long userId);

    Page<User> getUsers(int page, int size);

    Page<User> getUsersByRole(String role, int page, int size);
}
