package org.fantasymaps.backend.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.fantasymaps.backend.config.AppConfig;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "app_user")
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "username", nullable = false)
    @NotBlank(message = "Username is mandatory")
    @Pattern(
            regexp = AppConfig.usernamePattern,
            message = AppConfig.usernameMismatchMessage
    )
    private String username;
    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is mandatory")
    @Pattern(
            regexp = AppConfig.passwordPattern,
            message = AppConfig.passwordMismatchMessage
    )
    private String password;
    @Column(name = "email", nullable = false)
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email is invalid")
    private String email;
    @Column(name = "is_verified_email", nullable = false)
    private boolean isVerifiedEmail;
    @Column(name = "date_created", nullable = false)
    @NotNull(message = "Date created is mandatory")
    private LocalDate date;
}