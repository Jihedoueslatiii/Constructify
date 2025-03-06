package com.esprit.pi.constructify.entities;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GoogleUserDTO {
    private String firstName;
    private String lastName;
    private String email;
}
