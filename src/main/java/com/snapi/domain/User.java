package com.snapi.domain;

import com.snapi.dto.authentication.SignUpDTO;
import com.snapi.dto.user.UserUpdateDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "User")
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean deleted = false;
    
    @CreationTimestamp
    private LocalDate createdAt;
    
    @UpdateTimestamp
    private LocalDate updatedAt;
    
    private String name;
    private String username;
    private String phone;
    private String email;
    private String password;
    private String picLink;
    
    public User(SignUpDTO dto) {
        this.name = dto.name();
        this.username = dto.username();
        this.phone = dto.phone();
        this.email = dto.email();
        this.password = dto.password();
        this.picLink = dto.picLink();
    }
    
    public void update(UserUpdateDTO dto) {
        
        if (dto.name() != null) {
            this.name = dto.name();
        }
        
        if (dto.username() != null) {
            this.username = dto.username();
        }
        
        if (dto.phone() != null) {
            this.phone = dto.phone();
        }

        if (dto.email() != null) {
            this.email = dto.email();
        }
        
        if (dto.picLink() != null) {
            this.picLink = dto.picLink();
        }
        
    }

    public void delete() {this.deleted = true;}
    
    public void reactivate() {this.deleted = false;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

