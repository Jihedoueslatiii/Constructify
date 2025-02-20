package com.esprit.pi.constructify.services;

import com.esprit.pi.constructify.entities.Role;
import com.esprit.pi.constructify.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User saveUser(User user);
    List<User> getAllUsers();
    List<User> getUsersByRole(Role role);
    void deleteUser(Long userId);
    boolean existsByEmail(String email);
    String encodePassword(String password);
    Optional<User> findUserById(Long id);
    User getUserById(Long id);

}
