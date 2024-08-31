package org.fantasymaps.backend.controllers;

import org.fantasymaps.backend.dtos.AuthRequestDto;
import org.fantasymaps.backend.dtos.UserDto;
import org.fantasymaps.backend.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getUserById(Integer.parseInt(id)));
        } catch (Exception e) {
            logger.error("Error getting user: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserDto> authenticateUser(@RequestBody AuthRequestDto authRequestDto) {
        int userId;
        try {
            userId = userService.authenticateUser(authRequestDto);
            return ResponseEntity.ok(userService.getUserById(userId));
        } catch (Exception e) {
            logger.error("Error authenticating user: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
