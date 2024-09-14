package org.fantasymaps.backend.controllers;

import jakarta.servlet.http.HttpSession;
import org.fantasymaps.backend.dtos.AuthRequestDto;
import org.fantasymaps.backend.dtos.UserDto;
import org.fantasymaps.backend.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final SessionRepository<? extends Session> sessionRepository;

    @Autowired
    public UserController(UserService userService, SessionRepository<? extends Session> sessionRepository) {
        this.userService = userService;
        this.sessionRepository = sessionRepository;
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
    public ResponseEntity<UserDto> authenticateUser(@RequestBody AuthRequestDto authRequestDto, HttpSession httpSession) {
        int userId;
        try {
            userId = userService.authenticateUser(authRequestDto);
            UserDto user = userService.getUserById(userId);
            user.setToken(httpSession.getId());
            httpSession.setAttribute("user", user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error authenticating user: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
