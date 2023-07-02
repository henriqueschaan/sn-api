package com.snapi.controller;

import com.snapi.dto.post.PostCreationDTO;
import com.snapi.dto.post.PostDetailsDTO;
import com.snapi.dto.post.PostUpdateDTO;
import com.snapi.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    @Transactional
    public ResponseEntity<PostDetailsDTO> createPost(@Valid @RequestBody PostCreationDTO data, Authentication authentication) {
        return postService.createPost(data, authentication);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updatePost(@RequestBody @Valid PostUpdateDTO data, @PathVariable Long id, Authentication authentication) {
        return postService.updatePost(data, id, authentication);
    }

    // Endpoint para alterar estado de privacidade do post
    @PutMapping("/{id}/privacy")
    @Transactional
    public ResponseEntity<?> togglePostPrivacy(@PathVariable Long id, Authentication authentication) {
        return postService.togglePostPrivacy(id, authentication);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletePost(@PathVariable Long id, Authentication authentication) {
        return postService.deletePost(id, authentication);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByUserId(@PathVariable Long userId, Authentication authentication) {
        return postService.getPostsByUserId(userId, authentication);
    }

}