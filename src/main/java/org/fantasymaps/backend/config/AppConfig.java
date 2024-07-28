package org.fantasymaps.backend.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    public static final String usernamePattern = "^[a-zA-Z0-9_]{4,20}$";
    public static final String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
    public static final String namePattern = "^[a-zA-Z0-9 ]+$";
    public static final String urlPattern = ".*";

    public static final String usernameMismatchMessage = "Username must be 4-20 characters long and can only contain letters, numbers, and underscores";
    public static final String passwordMismatchMessage = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number";
    public static final String nameMismatchMessage = "Name must contain only letters, numbers, and spaces";
    public static final String urlMismatchMessage = "TODO";
}
