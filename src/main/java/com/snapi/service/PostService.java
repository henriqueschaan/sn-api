package com.snapi.service;

import com.snapi.domain.Post;
import com.snapi.domain.User;
import com.snapi.dto.post.PostCreationDTO;
import com.snapi.dto.post.PostDetailsDTO;
import com.snapi.dto.post.PostUpdateDTO;
import com.snapi.repository.PostRepository;
import com.snapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository repository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<PostDetailsDTO> createPost(PostCreationDTO data, Authentication authentication) {
        // Cria um post associando o usuário logado pelo id
        var post = new Post(data, userRepository, ((User) authentication.getPrincipal()).getId());
        repository.save(post);
        return ResponseEntity.ok(new PostDetailsDTO(post));
    }

    public ResponseEntity<PostDetailsDTO> detailPost(Long id) {
        var OpPost = repository.findById(id);
        Post post = OpPost.get();
        return ResponseEntity.ok(new PostDetailsDTO(post));
    }

    public ResponseEntity<?> updatePost(PostUpdateDTO data, Long id, Authentication authentication) {
        Optional<Post> optionalPost = repository.findById(id);

        // Verifica se um post com essa id existe
        if (optionalPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Post post = optionalPost.get();

        // Verifica se o post não foi deletado
        if (post.getDeleted()) {
            return ResponseEntity.status(HttpStatus.GONE).body("Post has been deleted");
        }

        // Verifica se o usuário autenticado é o criador do post
        if (!((User) authentication.getPrincipal()).getId().equals(post.getUser().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized");
        }

        post.updateInfo(data);
        return ResponseEntity.ok(new PostDetailsDTO(post));
    }

    public ResponseEntity<?> togglePostPrivacy(Long id, Authentication authentication) {
        Optional<Post> optionalPost = repository.findById(id);

        // Verifica se um post com essa id existe
        if (optionalPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Post post = optionalPost.get();

        // Verifica se o post não foi deletado
        if (post.getDeleted()) {
            return ResponseEntity.status(HttpStatus.GONE).body("Post has been deleted");
        }

        // Verifica se o usuário autenticado é o criador do post
        if (!((User) authentication.getPrincipal()).getId().equals(post.getUser().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized");
        }

        // Altera o estado de privacidade do post
        post.setIsPrivate(!post.getIsPrivate());

        return ResponseEntity.ok(new PostDetailsDTO(post));
    }

    public ResponseEntity<?> deletePost(Long id, Authentication authentication) {

        Optional<Post> optionalPost = repository.findById(id);

        // Verifica se um post com essa id existe
        if (optionalPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Post post = optionalPost.get();

        // Verifica se o usuário autenticado é o criador do post
        if (!((User) authentication.getPrincipal()).getId().equals(post.getUser().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized");
        }

        post.delete();
        return ResponseEntity.noContent().build();

    }

    public ResponseEntity<?> getPostsByUserId(Long userId, Authentication authentication) {
        Optional<User> optionalUser = userRepository.findById(userId);

        // Verifica se um usuário com essa id existe
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();

        // Verifica se o usuário não foi deletado
        if (user.getDeleted()) {
            return ResponseEntity.status(HttpStatus.GONE).body("User has been deleted");
        }

        List<Post> posts;

        // Se é o mesmo usuário, recupera todos os posts não-deletados
        if (((User) authentication.getPrincipal()).getId().equals(userId)) {
            posts = repository.findByUserIdAndDeletedFalseOrderByCreatedAtDesc(userId);
        }

        // Se é outro usuário, recupera posts não-privados e não-deletados
        else {
            posts = repository.findByUserIdAndIsPrivateFalseAndDeletedFalseOrderByCreatedAtDesc(userId);
        }

        // Transforma em DTOs e retorna
        List<PostDetailsDTO> dtos = posts.stream().map(PostDetailsDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<List<PostDetailsDTO>> getPostsByFriends(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<User> friends = userRepository.findFriendsById(userId);
        List<Post> posts = repository.findByUserInAndDeletedFalseOrderByCreatedAtDesc(friends);
        List<PostDetailsDTO> dtos = posts.stream().map(PostDetailsDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<List<PostDetailsDTO>> getAllActivePosts() {
        List<Post> posts = repository.findByDeletedFalseAndUser_DeletedFalseOrderByCreatedAtDesc();
        List<PostDetailsDTO> dtos = posts.stream().map(PostDetailsDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> likePost(Long id) {
        Post post = repository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setLikesCount(post.getLikesCount() + 1);
        repository.save(post);
        return ResponseEntity.ok(new PostDetailsDTO(post));
    }

}