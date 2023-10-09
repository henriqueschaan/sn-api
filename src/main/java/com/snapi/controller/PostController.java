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

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<PostDetailsDTO> detailPost(@PathVariable Long id) {
        return postService.detailPost(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updatePost(@RequestBody @Valid PostUpdateDTO data, @PathVariable Long id, Authentication authentication) {
        return postService.updatePost(data, id, authentication);
    }

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

    @GetMapping("/feed/{userId}")
    public ResponseEntity<List<PostDetailsDTO>> getPostsByFriends(@PathVariable Long userId) {
        return postService.getPostsByFriends(userId);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostDetailsDTO>> getAllActivePosts() {
        return postService.getAllActivePosts();
    }

    @PostMapping("/{id}/like")
    @Transactional
    public ResponseEntity<?> likePost(@PathVariable Long id) {
        return postService.likePost(id);
    }


}