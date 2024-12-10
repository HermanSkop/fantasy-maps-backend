package org.fantasymaps.backend.config;

import org.fantasymaps.backend.dtos.UserDto;
import org.fantasymaps.backend.model.user.Admin;
import org.fantasymaps.backend.model.user.Creator;
import org.fantasymaps.backend.model.user.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    public static final String usernamePattern = "^[a-zA-Z0-9_]{4,20}$";
    public static final String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
    public static final String namePattern = "^[a-zA-Z0-9 ]+$";

    public static final String usernameMismatchMessage = "Username must be 4-20 characters long and can only contain letters, numbers, and underscores";
    public static final String passwordMismatchMessage = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number";
    public static final String nameMismatchMessage = "Name must contain only letters, numbers, and spaces";

    public static final int nameMaxLength = 50;
    public static final int descriptionMaxLength = 1000;
    public static final int usernameMaxLength = 20;

    public static final int pageSize = 20;

    @Bean
    public ModelMapper modelMapper(UserConverter userConverter) {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(Admin.class, UserDto.class).setConverter(userConverter.adminConverter());
        modelMapper.typeMap(Customer.class, UserDto.class).setConverter(userConverter.customerConverter());
        modelMapper.typeMap(Creator.class, UserDto.class).setConverter(userConverter.creatorConverter());

        return modelMapper;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}