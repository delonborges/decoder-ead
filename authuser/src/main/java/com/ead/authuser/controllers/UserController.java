package com.ead.authuser.controllers;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userModelOptional = userService.find(userId);
        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userModelOptional = userService.find(userId);
        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } else {
            userService.delete(userModelOptional.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully.");
        }
    }
}
