package com.snapi.controller;

import com.snapi.domain.User;
import com.snapi.dto.authentication.LogInDTO;
import com.snapi.dto.authentication.SignUpDTO;
import com.snapi.dto.token.TokenDTO;
import com.snapi.dto.user.UserDetailsDTO;
import com.snapi.repository.UserRepository;
import com.snapi.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping
public class AuthenticationController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity createUser(@Valid @RequestBody SignUpDTO dto, UriComponentsBuilder uriBuilder) {
        
        // Verifica se username, email e telefone já não existem
        if (repository.existsByUsername(dto.username())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (repository.existsByEmail(dto.email())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        if (dto.phone() != null && repository.existsByPhone(dto.phone())) {
            return ResponseEntity.badRequest().body("Phone number already exists");
        }
        
        // Cria e salva o usuário com senha encriptada
        var user = new User(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserDetailsDTO(user));
        
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LogInDTO dto) {

        //Autentica o usuário e gera o token
        var authToken = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        var authentication = manager.authenticate(authToken);
        User user = (User) authentication.getPrincipal();
        var tokenJWT = tokenService.gerarToken(user);

        //Barra o login de usuário deletado
        if (user.getDeleted()) {
            return new ResponseEntity("User has been deleted", HttpStatus.FORBIDDEN);
        }

        //Retorna o token
        return ResponseEntity.ok(new TokenDTO(tokenJWT));
        
    }

}
