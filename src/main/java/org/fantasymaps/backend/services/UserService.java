package org.fantasymaps.backend.services;

import jakarta.persistence.EntityNotFoundException;
import org.fantasymaps.backend.controllers.UserController;
import org.fantasymaps.backend.dtos.AuthRequestDto;
import org.fantasymaps.backend.dtos.UserDto;
import org.fantasymaps.backend.repositories.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserDto getUserById(int id) {
            return modelMapper.map(userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("User not found")), UserDto.class);
    }

    public int authenticateUser(AuthRequestDto authRequestDto) {
        return userRepository.findByUsernameAndPassword(authRequestDto.getUsername(), authRequestDto.getPassword())
                .orElseThrow(() -> new EntityNotFoundException("User not found")).getId();
    }
}
