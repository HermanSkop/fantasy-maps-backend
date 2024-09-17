package org.fantasymaps.backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UserDto implements Serializable {
    @Pattern(message = "Username must be 4-20 characters long and can only contain letters, numbers, and underscores",
            regexp = "^[a-zA-Z0-9_]{4,20}$")
    @NotBlank(message = "Username is mandatory")
    String username;
    @Email(message = "Email is invalid")
    String email;
    boolean isVerifiedEmail = false;
    Role role;
    String token;
}