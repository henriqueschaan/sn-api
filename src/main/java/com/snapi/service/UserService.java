package com.snapi.service;

import com.snapi.domain.User;
import com.snapi.dto.user.UserDetailsDTO;
import com.snapi.dto.user.UserUpdateDTO;
import com.snapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository repository;
    
    public ResponseEntity detailUser(Long id) {

        Optional<User> optionalUser = repository.findById(id);

        // Verifica se um usuário com essa id existe
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();

        // Verifica se o usuário não foi deletado
        if (user.getDeleted()) {
            return ResponseEntity.status(HttpStatus.GONE).body("User has been deleted");
        }

        return new ResponseEntity(new UserDetailsDTO(user), HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(UserUpdateDTO dto, Long id, Authentication authentication) {

        Optional<User> optionalUser = repository.findById(id);
        
        // Verifica se um usuário com essa id existe
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        
        User user = optionalUser.get();
        
        // Verifica se o usuário não foi deletado
        if (user.getDeleted()) {
            return ResponseEntity.status(HttpStatus.GONE).body("User has been deleted");
        }
        
        // Verifica se o usuário autenticado é o mesmo a ser atualizado
        if (!((User) authentication.getPrincipal()).getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized");
        }

        // Verifica se username, email e telefone (se não-nulo) já não existem
        if (repository.existsByUsername(dto.username())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (repository.existsByEmail(dto.email())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        if (dto.phone() != null && repository.existsByPhone(dto.phone())) {
            return ResponseEntity.badRequest().body("Phone number already exists");
        }
        
        user.update(dto);
        return ResponseEntity.ok(new UserDetailsDTO(user));
    }

    public ResponseEntity<?> deleteUser(Long id, Authentication authentication) {
       
        Optional<User> optionalUser = repository.findById(id);
        
        // Verifica se um usuário com essa id existe
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        
        User user = optionalUser.get();
        
        // Verify if the authenticated user is the one they're deleting
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
    
}
