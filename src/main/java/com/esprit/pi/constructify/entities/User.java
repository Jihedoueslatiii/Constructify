package com.esprit.pi.constructify.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import jakarta.validation.constraints.*;
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstname;

    @NotBlank(message = "Last name is required")
    private String lastname;

    @Email(message = "Email must be valid")
    @Column(unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "The password must contain at least 8 characters, including one uppercase letter, one lowercase letter, one digit, and one special character.")
    private String password;

    @Pattern(regexp = "^[0-9]{8}$", message = "Phone number must contain exactly 8 digits")
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;
}
