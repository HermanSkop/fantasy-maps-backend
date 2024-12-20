package org.fantasymaps.backend.services;

import jakarta.persistence.EntityNotFoundException;
import org.fantasymaps.backend.config.AppConfig;
import org.fantasymaps.backend.controllers.UserController;
import org.fantasymaps.backend.dtos.AuthRequestDto;
import org.fantasymaps.backend.dtos.RegisterRequestDto;
import org.fantasymaps.backend.dtos.Role;
import org.fantasymaps.backend.dtos.UserDto;
import org.fantasymaps.backend.model.user.Creator;
import org.fantasymaps.backend.model.user.Customer;
import org.fantasymaps.backend.model.user.User;
import org.fantasymaps.backend.repositories.product.MapRepository;
import org.fantasymaps.backend.repositories.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final MapRepository mapRepository;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, MapRepository mapRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.mapRepository = mapRepository;
    }

    public UserDto getUserById(int id) {
        return modelMapper.map(userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found")), UserDto.class);
    }

    public int authenticateUser(AuthRequestDto authRequestDto) {
        return userRepository.findByUsernameAndPassword(authRequestDto.getUsername(), authRequestDto.getPassword()).orElseThrow(() -> new EntityNotFoundException("User not found")).getId();
    }

    public int registerUser(RegisterRequestDto registerRequestDto) {
        User user;
        if (registerRequestDto.getRole() == Role.CREATOR) user = modelMapper.map(registerRequestDto, Creator.class);
        else if (registerRequestDto.getRole() == Role.CUSTOMER)
            user = modelMapper.map(registerRequestDto, Customer.class);
        else
            throw new IllegalArgumentException("Invalid role");

        if (userRepository.findByUsername(user.getUsername()).isPresent())
            throw new IllegalArgumentException("Username already exists");
        if (!user.getPassword().matches(AppConfig.passwordPattern))
            throw new IllegalArgumentException(AppConfig.passwordMismatchMessage);

        user.setDate(LocalDate.now());
        return userRepository.save(user).getId();
    }


    public void favoriteMap(int mapId, int userId, boolean newStatus) {
        User user = userRepository.findById(userId).orElseThrow();
        Customer customer = (Customer) user;
        if (newStatus)
            customer.getFavoredProducts().add(mapRepository.findById(mapId).orElseThrow(() -> new IllegalArgumentException("Map not found")));
        else
            customer.getFavoredProducts().remove(mapRepository.findById(mapId).orElseThrow(() -> new IllegalArgumentException("Map not found")));
        userRepository.save(user);
    }

    public Boolean isFavorite(int id, int userId) {
        return userRepository.findById(userId).map(user -> {
            if (user.getClass() != Customer.class)
                throw new IllegalArgumentException("User is not a customer");
            Customer customer = (Customer) user;
            return customer.getFavoredProducts().stream().anyMatch(map -> map.getId() == id);
        }).orElse(false);
    }
}
