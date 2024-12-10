package org.fantasymaps.backend.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.fantasymaps.backend.config.AppConfig;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "username", length = AppConfig.usernameMaxLength)
    @NotBlank(message = "Username is mandatory")
    @Pattern(
            regexp = AppConfig.usernamePattern,
            message = AppConfig.usernameMismatchMessage
    )
    private String username;
    @Column(name = "password")
    @NotBlank(message = "Password is mandatory")
    @Pattern(
            regexp = AppConfig.passwordPattern,
            message = AppConfig.passwordMismatchMessage
    )
    private String password;
    @Column(name = "email")
    @Email(message = "Email is invalid")
    private String email;
    @Column(name = "is_verified_email")
    private boolean isVerifiedEmail;
    @Column(name = "date_created")
    @NotNull(message = "Date is mandatory")
    private LocalDate date;
}