package org.fantasymaps.backend.config;

import org.fantasymaps.backend.dtos.Role;
import org.fantasymaps.backend.dtos.UserDto;
import org.fantasymaps.backend.model.user.Admin;
import org.fantasymaps.backend.model.user.Creator;
import org.fantasymaps.backend.model.user.Customer;
import org.fantasymaps.backend.model.user.User;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    // Common conversion logic
    private UserDto mapUserToUserDto(User source) {
        UserDto destination = new UserDto();
        destination.setId(source.getId());
        destination.setUsername(source.getUsername());
        destination.setEmail(source.getEmail());
        destination.setVerifiedEmail(source.isVerifiedEmail());
        return destination;
    }

    // Admin converter
    public Converter<Admin, UserDto> adminConverter() {
        return context -> {
            UserDto destination = mapUserToUserDto(context.getSource());
            destination.setRole(Role.ADMIN);
            return destination;
        };
    }

    // Customer converter
    public Converter<Customer, UserDto> customerConverter() {
        return context -> {
            UserDto destination = mapUserToUserDto(context.getSource());
            destination.setRole(Role.CUSTOMER);
            return destination;
        };
    }

    // Creator converter
    public Converter<Creator, UserDto> creatorConverter() {
        return context -> {
            UserDto destination = mapUserToUserDto(context.getSource());
            destination.setRole(Role.CREATOR);
            return destination;
        };
    }
}
