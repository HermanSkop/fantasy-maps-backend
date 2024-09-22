package org.fantasymaps.backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fantasymaps.backend.config.AppConfig;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    int id;
    @Pattern(message = "Username must be 4-20 characters long and can only contain letters, numbers, and underscores",
            regexp = AppConfig.namePattern)
    @NotBlank(message = "Username is mandatory")
    String username;
    @Email(message = "Email is invalid")
    String email;
    boolean isVerifiedEmail = false;
    Role role;
    String token;
}