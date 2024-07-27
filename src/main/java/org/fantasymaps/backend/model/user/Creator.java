package org.fantasymaps.backend.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.fantasymaps.backend.config.AppConfig;

@Getter
@Setter
@Entity
public class Creator extends User {
    @Column(name = "avatar", nullable = false)
    @NotBlank(message = "Avatar url is mandatory")
    @Pattern(regexp = AppConfig.urlPattern, message = AppConfig.urlMismatchMessage)
    private String avatarUrl;
    @Column(name = "description", nullable = false)
    @NotBlank(message = "Description is mandatory")
    private String description;
}