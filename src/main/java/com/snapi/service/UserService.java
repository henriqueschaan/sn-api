package com.snapi.service;

import com.snapi.domain.Post;
import com.snapi.domain.User;
import com.snapi.dto.post.PostDetailsDTO;
import com.snapi.dto.user.UserDetailsDTO;
import com.snapi.dto.user.UserProfileDTO;
import com.snapi.dto.user.UserUpdateDTO;
import com.snapi.repository.PostRepository;
import com.snapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository repository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity detailUser(String username, Long loggedInUserId) {
        User user = repository.findUserByUsername(username);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        if (user.getDeleted()) return ResponseEntity.status(HttpStatus.GONE).body("User has been deleted");

        List<Post> userPosts = postRepository.findByUserIdAndDeletedFalseOrderByCreatedAtDesc(user.getId());
        List<PostDetailsDTO> postDetails = userPosts.stream().map(PostDetailsDTO::new).collect(Collectors.toList());

        int postCount = userPosts.size();
        int friendCount = user.getFriends().size();

        boolean isFriend = false;
        if (loggedInUserId != null) {
            User loggedInUser = repository.findById(loggedInUserId).orElse(null);
            if (loggedInUser != null) {
                isFriend = loggedInUser.getFriends().contains(user);
            }
        }

        UserProfileDTO userProfile = new UserProfileDTO(user, postCount, friendCount, isFriend);

        Map<String, Object> response = new HashMap<>();
        response.put("user", userProfile);
        response.put("posts", postDetails);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(UserUpdateDTO dto, Long id, Authentication authentication) {

        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isEmpty()) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");}
        User user = optionalUser.get();
        
        if (user.getDeleted()) {return ResponseEntity.status(HttpStatus.GONE).body("User has been deleted");}
        
        if (!((User) authentication.getPrincipal()).getId().equals(user.getId())) {return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized");}

        if ((!Objects.equals(dto.username(), user.getUsername())) && repository.existsByUsername(dto.username())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if ((!Objects.equals(dto.email(), user.getEmail())) && repository.existsByEmail(dto.email())) {
            System.out.println(dto.email());
            System.out.println(user.getEmail());
            return ResponseEntity.badRequest().body("Email already exists");
        }
        if ((!Objects.equals(dto.phone(), user.getPhone())) && (dto.phone() != null) && repository.existsByPhone(dto.phone())) {
            return ResponseEntity.badRequest().body("Phone number already exists");
        }

        if (dto.password() != null && !dto.password().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(dto.password());
            user.setPassword(encodedPassword);
        }

        user.update(dto);
        return ResponseEntity.ok(new UserDetailsDTO(user));
    }

    public ResponseEntity updateInfo(Long id, Authentication authentication) {
        Optional<User> optionalUser = repository.findById(id);
        User user = optionalUser.get();
        if (!((User) authentication.getPrincipal()).getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized");
        }

        return ResponseEntity.ok(new UserUpdateDTO(user));
    }

    public ResponseEntity<?> deleteUser(Long id, Authentication authentication) {
       
        Optional<User> optionalUser = repository.findById(id);
        
        // Verifica se um usuário com essa id existe
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        
        User user = optionalUser.get();

        if (!((User) authentication.getPrincipal()).getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized");
        }
        
        user.delete();
        return ResponseEntity.ok().build();
        
    }

    public ResponseEntity<List<UserDetailsDTO>> getAllUsers() {
        
        //Recupera todos os usuários não-deletados
        List<User> users = repository.findByDeletedFalse();
        
        //Transforma em DTOs e retorna
        List<UserDetailsDTO> dtos = users.stream().map(UserDetailsDTO::new).toList();
        return ResponseEntity.ok(dtos);
    
    }

    public ResponseEntity<?> addFriend(Long userId, Long friendId) {

        Optional<User> optionalUser = repository.findById(userId);
        if (optionalUser.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        User user = optionalUser.get();

        Optional<User> optionalFriend = repository.findById(friendId);
        if (optionalFriend.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friend not found");
        User friend = optionalFriend.get();

        user.addFriend(friend);
        repository.save(user);

        return ResponseEntity.ok("Friend added successfully");
    }

    public ResponseEntity<?> removeFriend(Long userId, Long friendId) {

        Optional<User> optionalUser = repository.findById(userId);
        if (optionalUser.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        User user = optionalUser.get();

        Optional<User> optionalFriend = repository.findById(friendId);
        if (optionalFriend.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friend not found");
        User friend = optionalFriend.get();

        user.removeFriend(friend);
        repository.save(user);

        return ResponseEntity.ok("Friend removed successfully");
    }

    public ResponseEntity<?> listFriends(Long id) {
        List<User> friends = repository.findFriendsById(id);
        List<UserDetailsDTO> dtos = friends.stream().map(UserDetailsDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

}
