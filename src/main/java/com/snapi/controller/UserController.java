package com.snapi.controller;

import com.snapi.dto.user.UserUpdateDTO;
import com.snapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity detailUser(@PathVariable Long id) {
        return userService.detailUser(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity updateUser(@RequestBody @Valid UserUpdateDTO dto, @PathVariable Long id, Authentication authentication) {
        return userService.updateUser(dto, id, authentication);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Authentication authentication) {
        return userService.deleteUser(id, authentication);
    }

    @GetMapping
    public ResponseEntity getAllUsers() {
        return userService.getAllUsers();
    }

}
