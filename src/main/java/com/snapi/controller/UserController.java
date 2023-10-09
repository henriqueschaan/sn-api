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

    @GetMapping("/{username}")
    public ResponseEntity detailUser(@PathVariable String username, @RequestParam(required = false) Long loggedInUserId) {
        return userService.detailUser(username, loggedInUserId);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity updateUser(@RequestBody @Valid UserUpdateDTO dto, @PathVariable Long id, Authentication authentication) {
        return userService.updateUser(dto, id, authentication);
    }

    @GetMapping("/update/{id}")
    public ResponseEntity updateInfo(@PathVariable Long id, Authentication authentication) {
        return userService.updateInfo(id, authentication);
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

    @PostMapping("/{userId}/addFriend/{friendId}")
    @Transactional
    public ResponseEntity<?> addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @PostMapping("/{userId}/removeFriend/{friendId}")
    @Transactional
    public ResponseEntity<?> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<?> listFriends(@PathVariable Long id) {
        return userService.listFriends(id);
    }

}
